package service;

import java.util.List;

import domain.Book;
import exception.PersistentException;

public interface BookService extends Service {
	List<Book> findByAuthor(Integer authorIdentity) throws PersistentException;

	List<Book> findByTitle(String search) throws PersistentException;

	Book findByIdentity(Integer identity) throws PersistentException;

	Book findByInventoryNumber(String inventoryNumber) throws PersistentException;

	void save(Book book) throws PersistentException;

	void delete(Integer identity) throws PersistentException;
}
