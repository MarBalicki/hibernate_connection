package com.balicki.hibernate;

import com.balicki.hibernate.model.Pet;
import com.balicki.hibernate.model.PetDao;
import com.balicki.hibernate.model.Race;

import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        PetDao dao = new PetDao();
        Scanner scanner = new Scanner(System.in);
        String order;

        do {
            System.out.println("Choose order: [add/list/update/byId/byRace/delete/quit]");
            order = scanner.nextLine();
            if (order.equalsIgnoreCase("add")) {
                addPet(dao, scanner);
            } else if (order.equalsIgnoreCase("list")) {
                showAllPets(dao);
            } else if (order.equalsIgnoreCase("update")) {
                updatePet(dao, scanner);
            } else if (order.equalsIgnoreCase("delete")) {
                deletePet(dao, scanner);
            } else if (order.equalsIgnoreCase("byID")) {
                findId(dao, scanner);
            } else if (order.equalsIgnoreCase("byRace")) {
                findRace(dao, scanner);
            }
        } while (!order.equalsIgnoreCase("quit"));

    }

    private static void findRace(PetDao dao, Scanner scanner) {
        System.out.println("Which race You are looking: ");
        String data = scanner.nextLine();
        Race race = Race.valueOf(data);
        System.out.println("Founded records: ");
        dao.findByRace(race).forEach(System.out::println);
    }

    private static void findId(PetDao dao, Scanner scanner) {
        System.out.println("Which ID You want to change: ");
        Long id = Long.valueOf(scanner.nextLine());
        Optional<Pet> petOptional = dao.findById(id);
        if (petOptional.isPresent()) {
            Pet pet = petOptional.get();
            System.out.println(pet.toString());
        } else {
            System.out.println("Record with that ID not exist!");
        }
    }

    private static void deletePet(PetDao dao, Scanner scanner) {
        System.out.println("Which ID You want to delete: ");
        Long id = Long.valueOf(scanner.nextLine());
        Optional<Pet> petOptional = dao.findById(id);
        if (petOptional.isPresent()) {
            Pet pet = petOptional.get();
            dao.delete(pet);
        } else {
            System.out.println("Record with that ID not exist!");
        }
    }

    private static void updatePet(PetDao dao, Scanner scanner) {
        System.out.println("Which ID You want to change: ");
        Long id = Long.valueOf(scanner.nextLine());
        Optional<Pet> petOptional = dao.findById(id);
        if (petOptional.isPresent()) {
            Pet pet = petOptional.get();
            System.out.println("You are tray to edit record: " + pet);
            System.out.println("Give data: NAME AGE OWNER_NAME WEIGHT isPURE_RACE RACE");
            String line = scanner.nextLine();
            String[] data = line.split(" ");
            pet = Pet.builder()
                    .name(data[0])
                    .age(Integer.parseInt(data[1]))
                    .ownerName(data[2])
                    .weight(Double.parseDouble(data[3]))
                    .pureRace(Boolean.parseBoolean(data[4]))
                    .race(Race.valueOf(data[5]))
                    .id(id)
                    .build();
            dao.saveOrUpdate(pet);
        } else {
            System.out.println("Error! Pet with that ID not exist!");
        }
    }

    private static void showAllPets(PetDao dao) {
        System.out.println("List of all pets: ");
        dao.getAllPets().forEach(System.out::println);
    }

    private static void addPet(PetDao dao, Scanner scanner) {
        System.out.println("Give data: NAME AGE OWNER_NAME WEIGHT isPURE_RACE RACE");
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
