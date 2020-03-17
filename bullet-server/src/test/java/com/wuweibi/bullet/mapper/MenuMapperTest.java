package com.wuweibi.bullet.mapper;

import com.wuweibi.bullet.entity.SysMenu;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MenuMapperTest {
    public class SampleTest {

        @Autowired
        private SysMenuMapper sysMenuMapper;

        @Test
        public void testSelect() {
            System.out.println(("----- selectAll method test ------"));
            List<SysMenu> list = sysMenuMapper.getOneLevelByUserId(1L);

            Assert.assertEquals(2, list.size());
            list.forEach(System.out::println);
        }

    }
}

