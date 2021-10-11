package suszombification.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;

public interface ZombifiedAnimal {
	EntityType<?> getNormalVariant();

	default void readFrom(Animal animal) {}
}