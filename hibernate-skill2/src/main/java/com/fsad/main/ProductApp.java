package com.fsad.main;

import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.fsad.entity.Product;
import com.fsad.util.HibernateUtil;

public class ProductApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Session session = HibernateUtil.getSessionFactory().openSession();

        while (true) {
            System.out.println("\n===== PRODUCT MENU =====");
            System.out.println("1. Create Product");
            System.out.println("2. Read Products");
            System.out.println("3. Update Product");
            System.out.println("4. Delete Product");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            Transaction tx = session.beginTransaction();

            switch (choice) {

                // CREATE
                case 1:
                    Product p = new Product();

                    System.out.print("Enter product name: ");
                    p.setName(sc.nextLine());

                    System.out.print("Enter description: ");
                    p.setDescription(sc.nextLine());

                    System.out.print("Enter price: ");
                    p.setPrice(sc.nextDouble());

                    System.out.print("Enter quantity: ");
                    p.setQuantity(sc.nextInt());

                    session.save(p);
                    tx.commit();

                    System.out.println("Product created successfully!");
                    break;

                // READ (NO ID SHOWN)
                case 2:
                    session.createQuery("from Product", Product.class)
                           .list()
                           .forEach(prod -> {
                               System.out.println("----------------------");
                               System.out.println("Name: " + prod.getName());
                               System.out.println("Description: " + prod.getDescription());
                               System.out.println("Price: " + prod.getPrice());
                               System.out.println("Quantity: " + prod.getQuantity());
                           });

                    tx.commit();
                    break;

                // UPDATE (by name)
                case 3:
                    System.out.print("Enter product name to update: ");
                    String updateName = sc.nextLine();

                    Product updateProd = session.createQuery(
                            "from Product where name = :name", Product.class)
                            .setParameter("name", updateName)
                            .uniqueResult();

                    if (updateProd != null) {
                        System.out.print("Enter new price: ");
                        updateProd.setPrice(sc.nextDouble());

                        System.out.print("Enter new quantity: ");
                        updateProd.setQuantity(sc.nextInt());

                        session.update(updateProd);
                        tx.commit();
                        System.out.println("Product updated successfully!");
                    } else {
                        tx.commit();
                        System.out.println("Product not found!");
                    }
                    break;

                // DELETE (by name)
                case 4:
                    System.out.print("Enter product name to delete: ");
                    String deleteName = sc.nextLine();

                    Product deleteProd = session.createQuery(
                            "from Product where name = :name", Product.class)
                            .setParameter("name", deleteName)
                            .uniqueResult();

                    if (deleteProd != null) {
                        session.delete(deleteProd);
                        tx.commit();
                        System.out.println("Product deleted successfully!");
                    } else {
                        tx.commit();
                        System.out.println("Product not found!");
                    }
                    break;

                // EXIT
                case 5:
                    tx.commit();
                    session.close();
                    sc.close();
                    System.out.println("Exiting application...");
                    System.exit(0);

                default:
                    tx.commit();
                    System.out.println("Invalid choice!");
            }
        }
    }
}
