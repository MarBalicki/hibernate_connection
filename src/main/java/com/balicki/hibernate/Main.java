package com.balicki.hibernate;

import com.balicki.hibernate.model.PetDao;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        PetDao dao = new PetDao();
        Scanner scanner = new Scanner(System.in);
        String order;

        do {
            System.out.println("Choose order: [add/list/update/delete/select/quit]");
            order = scanner.nextLine();
            if (order.equalsIgnoreCase("add")) {
                dao.addPet(scanner);
            } else if (order.equalsIgnoreCase("list")) {
                dao.showAllPets();
            } else if (order.equalsIgnoreCase("update")) {
                dao.updatePet(scanner);
            } else if (order.equalsIgnoreCase("delete")) {
                dao.deletePet(scanner);
            } else if (order.equalsIgnoreCase("select")) {
                selectBy(dao, scanner);
            }
        } while (!order.equalsIgnoreCase("quit"));
    }

    private static void selectBy(PetDao dao, Scanner scanner) {
        System.out.println("Choose select by: [id/race/pureRace/age/weight]");
        String select = scanner.nextLine();
        if (select.equalsIgnoreCase("id")) {
            dao.findPetId(scanner);
        } else if (select.equalsIgnoreCase("race")) {
            dao.findRace(scanner);
        } else if (select.equalsIgnoreCase("pureRace")) {
            dao.pureRace(scanner);
        } else if (select.equalsIgnoreCase("age")) {
            dao.findAge(scanner);
        } else if (select.equalsIgnoreCase("weight")) {
            dao.findWeight(scanner);
        }
    }

}
