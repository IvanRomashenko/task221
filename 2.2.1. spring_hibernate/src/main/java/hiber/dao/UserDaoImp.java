package hiber.dao;

import hiber.model.Car;
import hiber.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Objects;

@Repository
public class UserDaoImp implements UserDao {

    //    @Autowired
    private final SessionFactory sessionFactory;

    @Autowired
    public UserDaoImp(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void add(User user) {
        sessionFactory.getCurrentSession().save(user);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> listUsers() {
        TypedQuery<User> query = sessionFactory
                .getCurrentSession()
                .createQuery("select u from User u left join fetch u.car",User.class);
        return query.getResultList();
    }

    @Override
    public User getTheCarUserByModelAndSeries(String model, int series) {
//        String hql = "select u from User u where u.car.model=:model and u.car.series=:series";
//        TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery(hql, User.class);
//        query.setParameter("model", model).setParameter("series", series);
//        return query.setMaxResults(1).getSingleResult();

        String hql = "select u from User u join fetch u.car c where c.model=:model and c.series=:series";
        List<User> query = sessionFactory.getCurrentSession().createQuery(hql, User.class)
                .setParameter("model",model)
                .setParameter("series",series)
                .list();
        if(query.isEmpty()){
            System.out.println("car not found");
            return null;
        }else {
            System.out.println("car found");
            return query.get(0);
        }
    }
}
