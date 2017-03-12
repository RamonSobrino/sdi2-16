package uo.sdi.business.impl.admin.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import alb.util.date.DateUtil;
import uo.sdi.business.AdminService;
import uo.sdi.business.Services;
import uo.sdi.business.TaskService;
import uo.sdi.business.UserService;
import uo.sdi.business.exception.BusinessException;
import uo.sdi.business.impl.command.Command;
import uo.sdi.dto.Category;
import uo.sdi.dto.Task;
import uo.sdi.dto.User;
import uo.sdi.persistence.CategoryDao;
import uo.sdi.persistence.Persistence;
import uo.sdi.persistence.TaskDao;
import uo.sdi.persistence.UserDao;

public class InitDataBaseCommand implements Command<Void> {

	@Override
	public Void execute() throws BusinessException {
		UserDao uDao = Persistence.getUserDao();
		//TaskDao tDao = Persistence.getTaskDao();
		//CategoryDao cDao = Persistence.getCategoryDao();
		AdminService adminServices = Services.getAdminService();
		
		List<User> listaAnti = uDao.findAll();
		
		for(User us: listaAnti)
		{
			adminServices.deepDeleteUser(us.getId());
		}
		
		UserService userServices = Services.getUserService();
		TaskService taskServices = Services.getTaskService();
		
		
		
		String[] userLogin ={"admin1","user1","user2","user3"};
		String[] userPassword ={"admin1","user1","user2","user3"};
		String[] userMail ={"admin1@admin.com","user1@user.com","user2@user.com","user3@user.com"};
		boolean[] userIsAdmin={true,false,false,false};
		
		
		for(int i = 0;i<4;i++)
		{
			User u = new User();
			u.setLogin(userLogin[i]);
			u.setPassword(userPassword[i]);
			u.setEmail(userMail[i]);
			u.setIsAdmin(userIsAdmin[i]);
			
			userServices.addUser(u);
		}
		List<User> listaUsuarios = uDao.findAll();

		String[] nameCategory = {"categoría1", "categoría2", "categoría3"};
		
		long contadorCategorias =1;
		for(User u: listaUsuarios)
		{
			if(!u.getIsAdmin())
			{
				for(int i=0; i<3;i++)
				{
					Category cat = new Category();
					cat.setId(contadorCategorias);
					contadorCategorias++;
					cat.setName(nameCategory[i]);
					cat.setUserId(u.getId());
					
					taskServices.createCategory(cat);
				}	
			}
		}
		

		
		
		for(User u: listaUsuarios)
		{
			if(!u.getIsAdmin())
			{
				List<Category> listaCat = taskServices.findCategoriesByUserId(u.getId());
				
				for(int i=1;i<11;i++)
				{
				
					Task tarea = new Task();
					tarea.setUserId(u.getId());
					tarea.setTitle("Tarea " + i);
					tarea.setPlanned(DateUtil.today());
					taskServices.createTask(tarea);
				}
				
				for(int i =11;i<21;i++)
				{
					Task tarea = new Task();
					tarea.setUserId(u.getId());
					tarea.setTitle("Tarea " + i);
					tarea.setPlanned(DateUtil.addDays(DateUtil.today(), (int)Math.random()*5+1));
					taskServices.createTask(tarea);
				}
				
				for(int i =21;i<24;i++)
				{
					Task tarea = new Task();
					tarea.setUserId(u.getId());
					tarea.setTitle("Tarea " + i);
					tarea.setPlanned(DateUtil.subDays(DateUtil.today(), 3));
					
					tarea.setCategoryId(listaCat.get(0).getId());
					taskServices.createTask(tarea);
				}
				
				for(int i =24;i<27;i++)
				{
					Task tarea = new Task();
					tarea.setUserId(u.getId());
					tarea.setTitle("Tarea " + i);
					tarea.setPlanned(DateUtil.subDays(DateUtil.today(), 3));
					
					tarea.setCategoryId(listaCat.get(1).getId());
					taskServices.createTask(tarea);
				}
				for(int i =27;i<31;i++)
				{
					Task tarea = new Task();
					tarea.setUserId(u.getId());
					tarea.setTitle("Tarea " + i);
					tarea.setPlanned(DateUtil.subDays(DateUtil.today(), 3));
					
					tarea.setCategoryId(listaCat.get(2).getId());
					taskServices.createTask(tarea);
				}
			}
		}
			
		
		return null;
	}

}
