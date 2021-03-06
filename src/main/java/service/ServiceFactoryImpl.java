package service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import dao.Transaction;
import dao.TransactionFactory;
import exception.PersistentException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServiceFactoryImpl implements ServiceFactory {
	private static Logger logger = LogManager.getLogger(ServiceFactoryImpl.class);

	private static final Map<Class<? extends Service>, Class<? extends ServiceImpl>> SERVICES = new ConcurrentHashMap<>();

	static {
		SERVICES.put(AuthorService.class, AuthorServiceImpl.class);
		SERVICES.put(BookService.class, BookServiceImpl.class);
		SERVICES.put(UserService.class, UserServiceImpl.class);
		SERVICES.put(ReaderService.class, ReaderServiceImpl.class);
		SERVICES.put(UsageService.class, UsageServiceImpl.class);
	}

	private TransactionFactory factory;

	public ServiceFactoryImpl(TransactionFactory factory) throws PersistentException {
		this.factory = factory;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <Type extends Service> Type getService(Class<Type> key) throws PersistentException {
		Class<? extends ServiceImpl> value = SERVICES.get(key);
		if(value != null) {
			try {
				ClassLoader classLoader = value.getClassLoader();
				Class<?>[] interfaces = {key};
				Transaction transaction = factory.createTransaction();
				ServiceImpl service = value.newInstance();
				service.setTransaction(transaction);
				InvocationHandler handler = new ServiceInvocationHandlerImpl(service);
				return (Type)Proxy.newProxyInstance(classLoader, interfaces, handler);
			} catch(PersistentException e) {
				throw e;
			} catch(InstantiationException | IllegalAccessException e) {
				logger.error("It is impossible to instance service class", e);
				throw new PersistentException(e);
			}
		}
		return null;
	}

	@Override
	public void close() {
		factory.close();
	}
}
