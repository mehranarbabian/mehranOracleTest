package com.example.demo;
;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class Controller {
    UserDao userDao;

    public Controller(UserDao userDao) {
        this.userDao = userDao;
    }
    @RequestMapping(value = "/ws/insert",method = RequestMethod.POST)
    public void insert()
    {
        EntityUser entityUser=new EntityUser();
        entityUser.setName("mehran");
        entityUser.setSalary(200);
        userDao.save(entityUser);
    }
}
