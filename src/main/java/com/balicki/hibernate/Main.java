package com.balicki.hibernate;

import com.balicki.hibernate.model.Pet;
import com.balicki.hibernate.model.PetDao;
import com.balicki.hibernate.model.Race;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        PetDao dao = new PetDao();
        Scanner scanner = new Scanner(System.in);
        String order;

        do {
            System.out.println("Choose order: [add/list/update/delete]");
            order = scanner.nextLine();
            if (order.equalsIgnoreCase("add")) {
                addPet(dao, scanner);
            }

        } while (order.equalsIgnoreCase("quit"));

    }

    private static void addPet(PetDao dao, Scanner scanner) {
        System.out.println("Give data NAME AGE OWNER_NAME WEIGHT isPURE_RACE");
        String line = scanner.nextLine();
        String[] data = line.split(" ");
        Pet pet = Pet.builder()
                .name(data[0])
                .age(Integer.parseInt(data[1]))
                .ownerName(data[2])
                .weight(Double.parseDouble(data[3]))
                .pureRace(Boolean.parseBoolean(data[4]))
                .race(Race.valueOf(data[5]))
                .build();
        dao.saveOrUpdate(pet);
    }


}
