package demo;

import dataclay.api.BackendID;
import dataclay.api.DataClay;
import model.Person;

public class LMBackupDemo {

	public static boolean initialized = false;

	public static void main(final String args[]) throws Exception {
		// Init session in dataClay
		DataClay.init();
		System.out.println("** Starting demo **");

		// Get backend ids
		BackendID ds1BackendID = DataClay.getJBackendsByName().get("DS1").getDataClayID();
		BackendID ds2BackendID = DataClay.getJBackendsByName().get("DS2").getDataClayID();

		// Create object
		Person p = new Person("Elsa", 30);
		p.makePersistent("Elsa", ds1BackendID);

		// Create replica
		p.newReplica(ds2BackendID);
		System.out.println("Created replica in " + ds2BackendID);
		System.out.println("Input something when LM and DS1 are stopped");
		System.in.read();

		Person p2 = (Person) Person.getByAlias(Person.class.getName(), "Elsa");

		// Should run in replica location
		System.out.println("Name obtained from replica using backup metadata:" + p2.getName());
		System.out.println("Age obtained from replica using backup metadata:" + p2.getAge());

		System.out.println("** Demo finished! **");
		// Finish session in dataClay
		DataClay.finish();
	}

}