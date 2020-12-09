package action.registrar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import action.Action;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.AuthorService;
import exception.PersistentException;

public class AuthorDeleteAction extends RegistrarAction {
	//private static Logger logger = LogManager.getLogger(AuthorDeleteAction.class);

	@Override
	public Action.Forward exec(HttpServletRequest request, HttpServletResponse response) throws PersistentException {
		Forward forward = new Forward("/author/list.html");
		try {
			AuthorService service = factory.getService(AuthorService.class);
			Integer identity = Integer.parseInt(request.getParameter("identity"));
			service.delete(identity);
			forward.getAttributes().put("message", "Автор успешно удалён");
	//		logger.info(String.format("User \"%s\" deleted author with identity %d", getAuthorizedUser().getLogin(), identity));
		} catch(NumberFormatException e) {
	//		logger.warn(String.format("Incorrect data was found when user \"%s\" tried to delete author", getAuthorizedUser().getLogin()), e);
		}
		return forward;
	}
}
