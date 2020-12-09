package action.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import action.Action;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.ReaderService;
import exception.PersistentException;

public class ReaderDeleteAction extends AdministratorAction {
	//private static Logger logger = LogManager.getLogger(ReaderDeleteAction.class);

	@Override
	public Action.Forward exec(HttpServletRequest request, HttpServletResponse response) throws PersistentException {
		Forward forward = new Forward("/reader/list.html");
		try {
			ReaderService service = factory.getService(ReaderService.class);
			Integer identity = Integer.parseInt(request.getParameter("identity"));
			service.delete(identity);
			forward.getAttributes().put("message", "Читатель успешно удалён");
			//logger.info(String.format("User \"%s\" deleted reader with identity %d", getAuthorizedUser().getLogin(), identity));
		} catch(NumberFormatException e) {
			//logger.warn(String.format("Incorrect data was found when user \"%s\" tried to delete reader", getAuthorizedUser().getLogin()), e);
		}
		return forward;
	}
}
