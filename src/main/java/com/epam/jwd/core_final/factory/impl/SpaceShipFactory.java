package com.epam.jwd.core_final.factory.impl;

import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.factory.EntityFactory;

public class SpaceShipFactory implements EntityFactory<Spaceship> {
    @Override
    public Spaceship create(Object... args) {
        Spaceship spaceShip = new Spaceship((String) args[0]);
        spaceShip.setFlightDistance((Long) args[1]);
        spaceShip.setReadyForNextMissions((Boolean) args[3]);
        return spaceShip;
    }


}
