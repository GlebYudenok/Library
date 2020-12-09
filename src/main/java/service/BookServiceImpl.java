package service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.AuthorDao;
import dao.BookDao;
import dao.ReaderDao;
import dao.UsageDao;
import domain.Author;
import domain.Book;
import domain.Reader;
import domain.Usage;
import exception.PersistentException;

public class BookServiceImpl extends ServiceImpl implements BookService {
	@Override
	public List<Book> findByAuthor(Integer authorIdentity) throws PersistentException {
		BookDao bookDao = transaction.createDao(BookDao.class);
		List<Book> books = bookDao.readByAuthor(authorIdentity);
		buildBook(books);
		return books;
	}

	@Override
	public Book findByIdentity(Integer identity) throws PersistentException {
		BookDao bookDao = transaction.createDao(BookDao.class);
		Book book = bookDao.read(identity);
		if(book != null) {
			buildBook(Arrays.asList(book));
		}
		return book;
	}

	@Override
	public void save(Book book) throws PersistentException {
		BookDao dao = transaction.createDao(BookDao.class);
		if(book.getIdentity() != null) {
			dao.update(book);
		} else {
			book.setIdentity(dao.create(book));
		}
	}

	@Override
	public void delete(Integer identity) throws PersistentException {
		BookDao dao = transaction.createDao(BookDao.class);
		dao.delete(identity);
	}

	@Override
	public List<Book> findByTitle(String search) throws PersistentException {
		BookDao bookDao = transaction.createDao(BookDao.class);
		List<Book> books = bookDao.readByTitle(search);
		buildBook(books);
		return books;
	}

	@Override
	public Book findByInventoryNumber(String inventoryNumber) throws PersistentException {
		BookDao bookDao = transaction.createDao(BookDao.class);
		Book book = bookDao.readByInventoryNumber(inventoryNumber);
		if(book != null) {
			buildBook(Arrays.asList(book));
		}
		return book;
	}

	private void buildBook(List<Book> books) throws PersistentException {
		AuthorDao authorDao = transaction.createDao(AuthorDao.class);
		UsageDao usageDao = transaction.createDao(UsageDao.class);
		ReaderDao readerDao = transaction.createDao(ReaderDao.class);
		Map<Integer, Author> authors = new HashMap<>();
		List<Usage> usages;
		Map<Integer, Reader> readers = new HashMap<>();
		Author author;
		Integer identity;
		Reader reader;
		for(Book book : books) {
			author = book.getAuthor();
			if(author != null) {
				identity = author.getIdentity();
				author = authors.get(identity);
				if(author == null) {
					author = authorDao.read(identity);
				}
				book.setAuthor(author);
			}
			usages = usageDao.readByBook(book.getIdentity());
			for(Usage usage : usages) {
				if(usage.getReturnDate() == null) {
					book.setCurrentUsage(usage);
				} else {
					book.getUsages().add(usage);
				}
				identity = usage.getReader().getIdentity();
				reader = readers.get(identity);
				if(reader == null) {
					reader = readerDao.read(identity);
				}
				usage.setReader(reader);
			}
		}
	}
}
