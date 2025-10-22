package services;


public interface IRepository<T> {

    void add(T entity);
    void removeByNick(String nick);
    void removeByName(String name);
    T getByNick(String nick);
    T getByName(String name);
    void getAll();
    T updateByNick(String nick, T entity);
    T updateByName(String name, T entity);

}
