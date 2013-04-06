package pl.edu.agh.speedgame.dao;

import org.hibernate.*;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;

public class OurSessionReplacement implements AutoCloseable {
    private final Session session;
    private AtomicBoolean isClosed = new AtomicBoolean(false);

    public OurSessionReplacement(Session session) {
        this.session = session;
        this.session.beginTransaction();
    }

    @Override
    public void close() throws HibernateException {
        if(isClosed.compareAndSet(false, true)) {
            this.session.getTransaction().commit();
            session.close();
        }
    }

    public void cancelQuery() throws HibernateException {
        session.cancelQuery();
    }

    public Object load(String entityName, Serializable id, LockOptions lockOptions) {
        return session.load(entityName, id, lockOptions);
    }

    public Object load(Class theClass, Serializable id, LockOptions lockOptions) {
        return session.load(theClass, id, lockOptions);
    }

    public Object load(Class theClass, Serializable id) {
        return session.load(theClass, id);
    }

    public Object load(String entityName, Serializable id) {
        return session.load(entityName, id);
    }

    public void load(Object object, Serializable id) {
        session.load(object, id);
    }

    public void replicate(Object object, ReplicationMode replicationMode) {
        session.replicate(object, replicationMode);
    }

    public void replicate(String entityName, Object object, ReplicationMode replicationMode) {
        session.replicate(entityName, object, replicationMode);
    }

    public Serializable save(Object object) {
        return session.save(object);
    }

    public Serializable save(String entityName, Object object) {
        return session.save(entityName, object);
    }

    public void saveOrUpdate(Object object) {
        session.saveOrUpdate(object);
    }

    public void saveOrUpdate(String entityName, Object object) {
        session.saveOrUpdate(entityName, object);
    }

    public void update(Object object) {
        session.update(object);
    }

    public void update(String entityName, Object object) {
        session.update(entityName, object);
    }

    public Object merge(Object object) {
        return session.merge(object);
    }

    public Object merge(String entityName, Object object) {
        return session.merge(entityName, object);
    }

    public void persist(Object object) {
        session.persist(object);
    }

    public void persist(String entityName, Object object) {
        session.persist(entityName, object);
    }

    public void delete(Object object) {
        session.delete(object);
    }

    public void delete(String entityName, Object object) {
        session.delete(entityName, object);
    }

    public Object get(Class clazz, Serializable id) {
        return session.get(clazz, id);
    }

    public Object get(Class clazz, Serializable id, LockOptions lockOptions) {
        return session.get(clazz, id, lockOptions);
    }

    public Object get(String entityName, Serializable id) {
        return session.get(entityName, id);
    }

    public Object get(String entityName, Serializable id, LockOptions lockOptions) {
        return session.get(entityName, id, lockOptions);
    }

    public String getEntityName(Object object) {
        return session.getEntityName(object);
    }

    public Query getNamedQuery(String queryName) {
        return session.getNamedQuery(queryName);
    }

    public Query createQuery(String queryString) {
        return session.createQuery(queryString);
    }

    public SQLQuery createSQLQuery(String queryString) {
        return session.createSQLQuery(queryString);
    }

    public Criteria createCriteria(Class persistentClass) {
        return session.createCriteria(persistentClass);
    }

    public Criteria createCriteria(Class persistentClass, String alias) {
        return session.createCriteria(persistentClass, alias);
    }

    public Criteria createCriteria(String entityName) {
        return session.createCriteria(entityName);
    }

    public Criteria createCriteria(String entityName, String alias) {
        return session.createCriteria(entityName, alias);
    }
}
