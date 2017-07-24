package net.darmo_creations;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import net.darmo_creations.controllers.UndoRedoManagerTest;
import net.darmo_creations.model.family.FamilyMemberTest;
import net.darmo_creations.util.VersionTest;

@RunWith(Suite.class)
@SuiteClasses({VersionTest.class, FamilyMemberTest.class, UndoRedoManagerTest.class})
public class AllTests {}
