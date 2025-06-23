package com.mascotin.inventorymanagementapplication.model.catalogue;

import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.Required;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Specie {

    @Id @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, unique = true)
    @Required
    private String name;
}