package com.mascotin.inventorymanagementapplication.model;

import com.mascotin.inventorymanagementapplication.model.catalogue.AccessoryType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openxava.annotations.Required;
import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class PetAccessory extends Product {
    @Required
    @Enumerated(EnumType.STRING)
    private AccessoryType tipo;  // Juguete, Artículo de Aseo, Ropa
}
