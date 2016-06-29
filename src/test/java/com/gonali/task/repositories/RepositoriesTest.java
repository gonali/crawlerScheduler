package com.gonali.task.repositories;

import com.gonali.task.entityModel.CrawlerTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sun.tools.jar.CommandLine;

import java.util.List;

/**
 * Created by TianyuanPan on 6/29/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MockServletContext.class)
public class RepositoriesTest {


    //private CrawlerTaskRepository crawlerTaskRepository;

    @Test
    public void test_crawlerTaskRepository_findAll(){


    }
}
