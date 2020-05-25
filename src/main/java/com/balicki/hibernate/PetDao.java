package com.balicki.hibernate;

import com.balicki.hibernate.model.Pet;
import com.balicki.hibernate.model.Race;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class PetDao {

    public void addPet(Scanner scanner) {
        System.out.println("Give data: NAME AGE OWNER_NAME WEIGHT isPURE_RACE RACE");
        String line = scanner.nextLine();
        String[] data = line.split(" ");
        Pet pet = Pet.builder()
                .name(data[0])
                .age(Integer.parseInt(data[1]))
                .ownerName(data[2])
                .weight(Double.parseDouble(data[3]))
                .pureRace(Boolean.parseBoolean(data[4]))
                .race(Race.valueOf(data[5].toUpperCase()))
                .build();
        saveOrUpdate(pet);
    }

    public void updatePet(Scanner scanner) {
        System.out.println("Which ID You want to change: ");
        Long id = Long.valueOf(scanner.nextLine());
        Optional<Pet> petOptional = findPetById(id);
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
                    .race(Race.valueOf(data[5].toUpperCase()))
                    .id(id)
                    .build();
            saveOrUpdate(pet);
        } else {
            System.out.println("Error! Pet with that ID not exist!");
        }
    }

    private void saveOrUpdate(Pet pet) {
        SessionFactory sessionFactory = HibernateUtil.getOurSessionFactory();
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(pet);
            transaction.commit();
        } catch (HibernateException he) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public void showAllPets() {
        System.out.println("List of all pets: ");
        getAllPets().forEach(System.out::println);
    }

    private List<Pet> getAllPets() {
        List<Pet> list = new ArrayList<>();
        SessionFactory sessionFactory = HibernateUtil.getOurSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            Query<Pet> petQuery = session.createQuery("SELECT a FROM Pet a", Pet.class);
            list.addAll(petQuery.getResultList());
        } catch (HibernateException he) {
            he.printStackTrace();
        }
        return list;
    }

    public void deletePet(Scanner scanner) {
        System.out.println("Which ID You want to delete: ");
        Long id = Long.valueOf(scanner.nextLine());
        Optional<Pet> petOptional = findPetById(id);
        if (petOptional.isPresent()) {
            Pet pet = petOptional.get();
            delete(pet);
        } else {
            System.out.println("Record with that ID not exist!");
        }
    }

    private void delete(Pet pet) {
        SessionFactory sessionFactory = HibernateUtil.getOurSessionFactory();
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.delete(pet);
            transaction.commit();
        } catch (HibernateException he) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public void findPetId(Scanner scanner) {
        System.out.println("Which ID You want to change: ");
        Long id = Long.valueOf(scanner.nextLine());
        Optional<Pet> petOptional = findPetById(id);
        if (petOptional.isPresent()) {
            Pet pet = petOptional.get();
            System.out.println(pet.toString());
        } else {
            System.out.println("Record with that ID not exist!");
        }
    }

    private Optional<Pet> findPetById(Long id) {
        SessionFactory sessionFactory = HibernateUtil.getOurSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Pet.class, id));
        } catch (HibernateException he) {
            System.out.println("Record with that ID not exist!");
            he.printStackTrace();
        }
        return Optional.empty();
    }

    public void findRace(Scanner scanner) {
        System.out.println("Which race You are looking: ");
        String data = scanner.nextLine();
        Race race = Race.valueOf(data);
        System.out.println("Founded records: ");
        findByRace(race).forEach(System.out::println);
    }

    private List<Pet> findByRace(Race race) {
        List<Pet> list = new ArrayList<>();
        SessionFactory sessionFactory = HibernateUtil.getOurSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Pet> criteriaQuery = criteriaBuilder.createQuery(Pet.class);
            Root<Pet> rootTable = criteriaQuery.from(Pet.class);
            criteriaQuery.select(rootTable)
                    .where(criteriaBuilder.equal(rootTable.get("race"), race));
            list.addAll(session.createQuery(criteriaQuery).list());
        } catch (HibernateException he) {
            he.printStackTrace();
        }
        return list;
    }

    public void pureRace(Scanner scanner) {
        System.out.println("Choose true/false: ");
        String data = scanner.nextLine();
        Boolean pureRace = Boolean.parseBoolean(data.toUpperCase());
        System.out.println("Founded records: ");
        findByPureRace(pureRace).forEach(System.out::println);
    }

    private List<Pet> findByPureRace(Boolean isPure) {
        List<Pet> list = new ArrayList<>();
        SessionFactory sessionFactory = HibernateUtil.getOurSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Pet> criteriaQuery = criteriaBuilder.createQuery(Pet.class);
            Root<Pet> rootTable = criteriaQuery.from(Pet.class);
            criteriaQuery.select(rootTable)
                    .where(criteriaBuilder.equal(rootTable.get("pureRace"), isPure));
            list.addAll(session.createQuery(criteriaQuery).list());
        } catch (HibernateException he) {
            he.printStackTrace();
        }
        return list;
    }

    public void findAge(Scanner scanner) {
        System.out.println("Write [ageFrom ageTo]: ");
        String line = scanner.nextLine();
        String[] data = line.split(" ");
        int ageFrom = Integer.parseInt(data[0]);
        int ageTo = Integer.parseInt(data[1]);
        System.out.println("Founded records: ");
        findByAge(ageFrom, ageTo).forEach(System.out::println);
    }

    private List<Pet> findByAge(int ageFrom, int ageTo) {
        List<Pet> list = new ArrayList<>();
        SessionFactory sessionFactory = HibernateUtil.getOurSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Pet> criteriaQuery = criteriaBuilder.createQuery(Pet.class);
            Root<Pet> rootTable = criteriaQuery.from(Pet.class);
            criteriaQuery.select(rootTable)
                    .where(criteriaBuilder.between(rootTable.get("age"), ageFrom, ageTo));
            list.addAll(session.createQuery(criteriaQuery).list());
        } catch (HibernateException he) {
            he.printStackTrace();
        }
        return list;
    }

    public void findWeight(Scanner scanner) {
        System.out.println("Write [weightFrom weightTo]: ");
        String line = scanner.nextLine();
        String[] data = line.split(" ");
        double weightFrom = Double.parseDouble(data[0]);
        double weightTo = Double.parseDouble(data[1]);
        System.out.println("Founded records: ");
        findByWeight(weightFrom, weightTo).forEach(System.out::println);
    }

    private List<Pet> findByWeight(double weightFrom, double weightTo) {
        List<Pet> list = new ArrayList<>();
        SessionFactory sessionFactory = HibernateUtil.getOurSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Pet> criteriaQuery = criteriaBuilder.createQuery(Pet.class);
            Root<Pet> rootTable = criteriaQuery.from(Pet.class);
            criteriaQuery.select(rootTable)
                    .where(criteriaBuilder.between(rootTable.get("weight"), weightFrom, weightTo));
            list.addAll(session.createQuery(criteriaQuery).list());
        } catch (HibernateException he) {
            he.printStackTrace();
        }
        return list;
    }
}
