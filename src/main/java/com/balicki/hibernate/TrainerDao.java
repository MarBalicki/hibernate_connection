package com.balicki.hibernate;

import com.balicki.hibernate.model.Pet;
import com.balicki.hibernate.model.Specialization;
import com.balicki.hibernate.model.Trainer;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;

public class TrainerDao {

    public void addTrainer(Scanner scanner) {
        System.out.println("Give data: NAME LAST_NAME SPECIALIZATION " +
                "[PSYCHOLOGY, BEHAVIORISM, AGGRESSION_MANAGEMENT, TRICK_TRAINING, OBEDIENCE]");
        String line = scanner.nextLine();
        String[] data = line.split(" ");
        Trainer trainer = Trainer.builder()
                .firstName(data[0])
                .lastName(data[1])
                .specialization(Specialization.valueOf(data[2].toUpperCase()))
                .build();
        saveOrUpdate(trainer);
    }

    public void updateTrainer(Scanner scanner) {
        System.out.println("Which ID You want to update: ");
        Long id = Long.valueOf(scanner.nextLine());
        Optional<Trainer> optionalTrainer = findById(id);
        if (optionalTrainer.isPresent()) {
            Trainer trainer = optionalTrainer.get();
            System.out.println("You are trying to edit record: " + trainer);
            System.out.println("Give data: NAME LAST_NAME SPECIALIZATION " +
                    "[PSYCHOLOGY, BEHAVIORISM, AGGRESSION_MANAGEMENT, TRICK_TRAINING, OBEDIENCE]");
            String line = scanner.nextLine();
            String[] data = line.split(" ");
            trainer = Trainer.builder()
                    .firstName(data[0])
                    .lastName(data[1])
                    .specialization(Specialization.valueOf(data[2].toUpperCase()))
                    .id(id)
                    .build();
            saveOrUpdate(trainer);
        } else {
            System.out.println("Error! Pet with that ID not exist!");
        }
    }

    private void saveOrUpdate(Trainer trainer) {
        SessionFactory sessionFactory = HibernateUtil.getOurSessionFactory();
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(trainer);
            transaction.commit();
        } catch (HibernateException he) {
            he.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public void showAllTrainer() {
        System.out.println("List of trainers: ");
        getAllTrainers().forEach(System.out::println);
    }

    public void findTrainerId(Scanner scanner) {
        System.out.println("Which ID You want to change: ");
        Long id = Long.valueOf(scanner.nextLine());
        Optional<Trainer> optionalTrainer = findById(id);
        if (optionalTrainer.isPresent()) {
            Trainer trainer = optionalTrainer.get();
            System.out.println(trainer.toString());
        } else {
            System.out.println("Trainer whit that ID not exist!");
        }
    }

    private Optional<Trainer> findById(Long id) {
        SessionFactory sessionFactory = HibernateUtil.getOurSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Trainer.class, id));
        } catch (HibernateException he) {
            he.printStackTrace();
        }
        return Optional.empty();
    }

    private List<Trainer> getAllTrainers() {
        List<Trainer> list = new ArrayList<>();
        SessionFactory sessionFactory = HibernateUtil.getOurSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Query<Trainer> trainerQuery = session.createQuery("SELECT a FROM Trainer a", Trainer.class);
            list.addAll(trainerQuery.getResultList());
        } catch (HibernateException he) {
            he.printStackTrace();
        }
        return list;
    }

    public void deleteTrainer(Scanner scanner) {
        System.out.println("Which ID You want to delete: ");
        Long id = Long.valueOf(scanner.nextLine());
        Optional<Trainer> optionalTrainer = findById(id);
        if (optionalTrainer.isPresent()) {
            Trainer trainer = optionalTrainer.get();
            delete(trainer);
        } else {
            System.out.println("Record with that ID not exist!");
        }
    }

    private void delete(Trainer trainer) {
        SessionFactory sessionFactory = HibernateUtil.getOurSessionFactory();
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.delete(trainer);
            transaction.commit();
        } catch (HibernateException he) {
            he.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public void findSpecialization(Scanner scanner) {
        System.out.println("Which specialization You are looking for: " +
                "[PSYCHOLOGY, BEHAVIORISM, AGGRESSION_MANAGEMENT, TRICK_TRAINING, OBEDIENCE]");
        String data = scanner.nextLine();
        Specialization specialization = Specialization.valueOf(data);
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

    public Set<Pet> findPets(Long id) {
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
