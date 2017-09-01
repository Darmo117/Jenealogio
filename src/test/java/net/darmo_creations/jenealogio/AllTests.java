package net.darmo_creations.jenealogio;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import net.darmo_creations.jenealogio.controllers.UndoRedoManagerTest;
import net.darmo_creations.jenealogio.model.family.FamilyMemberTest;
import net.darmo_creations.jenealogio.util.VersionTest;

@RunWith(Suite.class)
@SuiteClasses({VersionTest.class, FamilyMemberTest.class, UndoRedoManagerTest.class})
public class AllTests {}
