/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.udya.querydsl.cuba.core.domain;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The Class Cat.
 */
@Entity(name = "querydslcuba_Cat")
public class Cat extends Animal {

    private static final long serialVersionUID = - 713086438605899938L;

    @Column(name = "BREED")
    private Integer breed;

    @Column(name = "EYECOLOR")
    private String eyecolor;

    @OneToMany(mappedBy = "mate")
    private List<Cat> kittens = new ArrayList<>();

    @OneToMany
    @JoinTable(name = "QUERYDSL_CUBA_KITTENS_SET",
            joinColumns = @JoinColumn(name = "cat_id"), inverseJoinColumns = @JoinColumn(name = "kitten_id"))
    private Set<Cat> kittensSet;

//    @OneToMany
//    @JoinTable(name="kittens_array")
//    @IndexColumn(name = "arrayIndex")
//    private Cat[] kittensArray = new Cat[0];

    @ManyToOne
    @JoinColumn(name = "CAT_ID")
    private Cat mate;

    public List<Cat> getKittens() {
        return kittens;
    }

    public Integer getBreed() {
        return breed;
    }

    public static Cat cat() {
        Metadata metadata = AppBeans.get(Metadata.class);
        return metadata.create(Cat.class);
    }

    public static Cat cat(Integer id) {
        Metadata metadata = AppBeans.get(Metadata.class);
        Cat cat = metadata.create(Cat.class);
        cat.setIntId(id);

        return cat;
    }

    public static Cat cat(String name, Integer id) {
        Metadata metadata = AppBeans.get(Metadata.class);
        Cat cat = metadata.create(Cat.class);

        cat.setIntId(id);
        cat.setName(name);

        return cat;
    }

    public static Cat cat(String name, Integer id, Color color) {
        Metadata metadata = AppBeans.get(Metadata.class);
        Cat cat = metadata.create(Cat.class);

        cat.setIntId(id);
        cat.setName(name);
        cat.setColor(color);

        return cat;
    }

    public static Cat cat (String name, Integer id, List<Cat> k) {
        Metadata metadata = AppBeans.get(Metadata.class);
        Cat cat = metadata.create(Cat.class);

        cat.setIntId(id);
        cat.setName(name);
        cat.kittens.addAll(k);

        return cat;
    }

    public static Cat cat(String name, Integer id, double bodyWeight) {
        Cat cat = Cat.cat(name, id);
        cat.setBodyWeight(bodyWeight);
        cat.setDoubleProperty(bodyWeight);

        return cat;
    }

    public Color getEyecolor() {
        return eyecolor == null ? null : Color.fromId(eyecolor);
    }

    public Cat getMate() {
        return mate;
    }

//    public Cat[] getKittensArray() {
//        return kittensArray;
//    }

    public void addKitten(Cat kitten) {
        kittens.add(kitten);
//        kittensArray = new Cat[]{kitten};
    }

    public Set<Cat> getKittensSet() {
        return kittensSet;
    }

    public void setKittensSet(Set<Cat> kittensSet) {
        this.kittensSet = kittensSet;
    }

    public void setMate(Cat mate) {
        this.mate = mate;
    }

}

