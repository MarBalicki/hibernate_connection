package com.balicki.hibernate;

import com.balicki.hibernate.model.Pet;
import com.balicki.hibernate.model.Specialization;
import com.balicki.hibernate.model.Trainer;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;

public class TrainerDao {
    private EntityDao<Trainer> entityDao = new EntityDao<>();

    protected void addTrainer(Scanner scanner) {
        System.out.println("Give data: NAME LAST_NAME SPECIALIZATION " +
                "[PSYCHOLOGY, BEHAVIORISM, AGGRESSION_MANAGEMENT, TRICK_TRAINING, OBEDIENCE, MAKING_POO_IN_NEIGHBOR_GARDEN]");
        String line = scanner.nextLine();
        String[] data = line.split(" ");
        try {
            Trainer trainer = Trainer.builder()
                    .firstName(data[0])
                    .lastName(data[1])
                    .specialization(Specialization.valueOf(data[2].toUpperCase()))
                    .build();
            entityDao.saveOrUpdate(trainer);
        } catch (Exception e) {
            System.out.println("Bad data!");
        }
    }

    protected void updateTrainer(Scanner scanner) {
        System.out.println("Which ID You want to update: ");
        Long id = Long.valueOf(scanner.nextLine());
        Optional<Trainer> optionalTrainer = entityDao.findById(Trainer.class, id);
        if (optionalTrainer.isPresent()) {
            try {
                Trainer trainer = optionalTrainer.get();
                System.out.println("You are trying to edit record: " + trainer);
                System.out.println("Give data: NAME LAST_NAME SPECIALIZATION " +
                        "[PSYCHOLOGY, BEHAVIORISM, AGGRESSION_MANAGEMENT, TRICK_TRAINING, OBEDIENCE, MAKING_POO_IN_NEIGHBOR_GARDEN]");
                String line = scanner.nextLine();
                String[] data = line.split(" ");
                trainer = Trainer.builder()
                        .firstName(data[0])
                        .lastName(data[1])
                        .specialization(Specialization.valueOf(data[2].toUpperCase()))
                        .id(id)
                        .build();
                entityDao.saveOrUpdate(trainer);
            } catch (Exception e) {
                System.out.println("Bad data!");
            }
        } else {
            System.out.println("Error! Trainer with that ID not exist!");
        }
    }

    protected void showAllTrainer() {
        System.out.println("List of trainers: ");
        entityDao.getAll(Trainer.class).forEach(System.out::println);
    }

    protected void findTrainerId(Scanner scanner) {
        System.out.println("Which ID are You looking: ");
        Long id = Long.valueOf(scanner.nextLine());
        Optional<Trainer> optionalTrainer = entityDao.findById(Trainer.class, id);
        if (optionalTrainer.isPresent()) {
            Trainer trainer = optionalTrainer.get();
            System.out.println(trainer.toString());
        } else {
            System.out.println("Trainer whit that ID not exist!");
        }
    }

    protected void deleteTrainer(Scanner scanner) {
        System.out.println("Which ID You want to delete: ");
        Long id = Long.valueOf(scanner.nextLine());
        Optional<Trainer> optionalTrainer = entityDao.findById(Trainer.class, id);
        if (optionalTrainer.isPresent()) {
            Trainer trainer = optionalTrainer.get();
            entityDao.delete(trainer);
        } else {
            System.out.println("Record with that ID not exist!");
        }
    }

    protected void findSpecialization(Scanner scanner) {
        System.out.println("Which specialization are You looking for: " +
                "[PSYCHOLOGY, BEHAVIORISM, AGGRESSION_MANAGEMENT, TRICK_TRAINING, OBEDIENCE, MAKING_POO_IN_NEIGHBOR_GARDEN]");
        String data = scanner.nextLine();
        Specialization specialization = Specialization.valueOf(data.toUpperCase());
        System.out.println("Founded records: ");
        findBySpecialization(specialization).forEach(System.out::println);
    }

    private List<Trainer> findBySpecialization(Specialization specialization) {
        List<Trainer> list = new ArrayList<>();
        SessionFactory sessionFactory = HibernateUtil.getOurSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Trainer> criteriaQuery = criteriaBuilder.createQuery(Trainer.class);
            Root<Trainer> trainerRoot = criteriaQuery.from(Trainer.class);
            criteriaQuery.select(trainerRoot)
                    .where(criteriaBuilder.equal(trainerRoot.get("specialization"), specialization));
            list.addAll(session.createQuery(criteriaQuery).list());
        } catch (HibernateException he) {
            he.printStackTrace();
        }
        return list;
    }

    protected void trainerListOfPets(Scanner scanner) {
        System.out.println("Give trainer ID: ");
        Long id = Long.valueOf(scanner.nextLine());
        try {
            Optional<Trainer> trainerOptional = entityDao.findById(Trainer.class, id);
            if (trainerOptional.isPresent()) {
                findPets(id).forEach(System.out::println);
            } else {
                System.out.println("There is no trainer with that ID!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Set<Pet> findPets(Long id) {
        Set<Pet> pets = new HashSet<>();
        SessionFactory sessionFactory = HibernateUtil.getOurSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Trainer trainer = session.get(Trainer.class, id);
            pets.addAll(trainer.getPetSet());
        } catch (HibernateException he) {
            he.printStackTrace();
        }
        return pets;
    }
}
