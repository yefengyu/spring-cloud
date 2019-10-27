package com.yefengyu.cloud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PersonController {

    @GetMapping("/persons")
    public List<Person> listPerson(){
        List<Person> personList = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            Person person = new Person();
            person.setId(i);
            person.setName("王五" + i);
            person.setAddress("北京" + i);
            personList.add(person);
        }
        return personList;
    }

    public static class Person{
        private Integer id;
        private String name;
        private String address;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", address='" + address + '\'' +
                    '}';
        }
    }
}


