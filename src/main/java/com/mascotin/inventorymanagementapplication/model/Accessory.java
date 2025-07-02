package com.mascotin.inventorymanagementapplication.model;

import com.mascotin.inventorymanagementapplication.model.catalogue.AccessoryType;
import lombok.Getter;
import lombok.Setter;
import org.openxava.annotations.Required;
import javax.persistence.*;

@Entity
@Getter @Setter
public class Accessory extends Product {
    @Required
    @Enumerated(EnumType.STRING)
    private AccessoryType Tipo;  // Juguete, Artículo de Aseo, Ropa
}
