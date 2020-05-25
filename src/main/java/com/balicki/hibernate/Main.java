package com.balicki.hibernate;

import com.balicki.hibernate.model.Pet;
import com.balicki.hibernate.model.Trainer;

import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        PetDao daoP = new PetDao();
        TrainerDao daoT = new TrainerDao();
        Scanner scanner = new Scanner(System.in);
        String order;

        do {
            System.out.println("Choose order: [addPet/addTrainer/petList/trainerList/updatePet/updateTrainer" +
                    "/deletePet/deleteTrainer/connectTrainer/selectPet/selectTrainer/quit]");
            order = scanner.nextLine();
            if (order.equalsIgnoreCase("addPet")) {
                daoP.addPet(scanner);
            } else if (order.equalsIgnoreCase("addTrainer")) {
                daoT.addTrainer(scanner);
            } else if (order.equalsIgnoreCase("petList")) {
                daoP.showAllPets();
            } else if (order.equalsIgnoreCase("trainerList")) {
                daoT.showAllTrainer();
            } else if (order.equalsIgnoreCase("updatePet")) {
                daoP.updatePet(scanner);
            } else if (order.equalsIgnoreCase("updateTrainer")) {
                daoT.updateTrainer(scanner);
            } else if (order.equalsIgnoreCase("deletePet")) {
                daoP.deletePet(scanner);
            } else if (order.equalsIgnoreCase("deleteTrainer")) {
                daoT.deleteTrainer(scanner);
            } else if (order.equalsIgnoreCase("connectTrainer")) {
                connectTrainer(scanner);
            } else if (order.equalsIgnoreCase("selectPet")) {
                selectPetBy(daoP, scanner);
            } else if (order.equalsIgnoreCase("selectTrainer")) {
                selectTrainerBy(daoT, scanner);
            }
        } while (!order.equalsIgnoreCase("quit"));
    }

    private static void selectPetBy(PetDao dao, Scanner scanner) {
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
        } else {
            System.out.println("Wrong order!");
        }
    }

    private static void selectTrainerBy(TrainerDao dao, Scanner scanner) {
        System.out.println("Choose select by: [id/specialization/trainerPets]");
        String select = scanner.nextLine();
        if (select.equalsIgnoreCase("id")) {
            dao.findTrainerId(scanner);
        } else if (select.equalsIgnoreCase("specialization")) {
            dao.findSpecialization(scanner);
        } else if (select.equalsIgnoreCase("trainerPets")) {
            dao.trainerListOfPets(scanner);
        } else {
            System.out.println("Wrong order!");
        }
    }

    private static void connectTrainer(Scanner scanner) {
        System.out.println("Give me pet ID: ");
        Long petId = Long.valueOf(scanner.nextLine());
        System.out.println("Give me trainer ID: ");
        Long trainerId = Long.valueOf(scanner.nextLine());
        Optional<Pet> petOptional = new EntityDao<Pet>().findById(Pet.class, petId);
        if (petOptional.isPresent()) {
            Pet pet = petOptional.get();
            Optional<Trainer> trainerOptional = new EntityDao<Trainer>().findById(Trainer.class, trainerId);
            if (trainerOptional.isPresent()) {
                Trainer trainer = trainerOptional.get();
                pet.getTrainerSet().add(trainer);
                new EntityDao<Pet>().saveOrUpdate(pet);
            }
        } else {
            System.err.println("Error! There is no pet with that ID!");
        }
    }

}
