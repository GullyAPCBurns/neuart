/**
 * CLI to perform administrative tasks with atlases in the KB.
 * 
 */
package edu.isi.bmkeg.neuart.atlasserver.admin;

import java.io.File;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AtlasAdmin {
	
	static final private String CMD_LOAD_ATLAS = "load";		// load atlas command
	static final private String CMD_HELP = "help";				// help command
	static final private String CMD_LIST_ATLASES = "list";		// list atlases command
	static final private String CMD_DELETE_ATLAS = "delete";	// delete atlas command
	static final private String CMD_LOAD_DATA_MAP = "loaddm";	// load data map command
	static final private String CMD_LOAD_DATA_MAPS = "loaddms";	// load data maps from directory command
	static final private String CMD_LIST_DATA_MAPS = "listdms";	// list datamaps command
	static final private String CMD_LIST_DATA_MAP = "listdm";	// list one datamap command
	static final private String CMD_DELETE_DATA_MAP = "deletedm";	// delete datamap command
	static final private String CMD_LOAD_BRAIN_REGIONS = "loadbr";	// load brain regions command
	
	private AtlasAdminCommands commands;
	
	// Command parameters
	
	static private String className = "AtlasAdmin";
	
	private String currentAction;		// Action to be performed passed as an argument in the command line 
	private File file;					// filename passed as argument in the command line
	private String atlasURI;			// atlas URL passed as argument in the command line
	private String datamapURI;			// atlas URL passed as argument in the command line

	private static void printusage() {
		System.out.println("Usage:");
		System.out.println(className + " " + CMD_LOAD_ATLAS + " atlasxml");
		System.out.println(className + " " + CMD_LIST_ATLASES);
		System.out.println(className + " " + CMD_LIST_ATLASES + " atlasuri");
		System.out.println(className + " " + CMD_DELETE_ATLAS + " atlasuri");
		System.out.println(className + " " + CMD_LOAD_DATA_MAP + " datamapxml");
		System.out.println(className + " " + CMD_LOAD_DATA_MAPS + " directory");
		System.out.println(className + " " + CMD_LIST_DATA_MAPS + "atlasuri");
		System.out.println(className + " " + CMD_LIST_DATA_MAP + "datamapuri");
		System.out.println(className + " " + CMD_DELETE_DATA_MAP + " datamapuri");
		System.out.println(className + " " + CMD_LOAD_BRAIN_REGIONS + " brainregionsxml");
		System.out.println(className + " " + CMD_HELP);
	}

	AtlasAdmin(AtlasAdminCommands commands) {
		this.commands = commands;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
        		"edu/isi/bmkeg/neuart/atlasserver/admin/adminApplicationContext.xml");
        AtlasAdminCommands commands = applicationContext.getBean(AtlasAdminCommands.class);

		AtlasAdmin am = new AtlasAdmin(commands);
		if (!am.parseArguments(args)) {
			printusage();
			System.exit(1);
		}
		try {
			am.execute();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
    }

	private void execute() throws Exception {
		if (currentAction.equals(CMD_HELP))
			printusage();
		else if (currentAction.equals(CMD_LOAD_ATLAS))
			commands.loadAtlas(file);
		else if (currentAction.equals(CMD_LIST_ATLASES))
			if (atlasURI == null) commands.listAtlases();
			else commands.listAtlases(atlasURI);
		else if (currentAction.equals(CMD_DELETE_ATLAS))
			commands.deleteAtlas(atlasURI);
		else if (currentAction.equals(CMD_LOAD_DATA_MAP))
			commands.loadDataMap(file);
		else if (currentAction.equals(CMD_LOAD_DATA_MAPS))
			commands.loadDataMaps(file);
		else if (currentAction.equals(CMD_LIST_DATA_MAPS))
			commands.listDatamaps(atlasURI);
		else if (currentAction.equals(CMD_LIST_DATA_MAP))
			commands.listDatamap(datamapURI);
		else if (currentAction.equals(CMD_DELETE_DATA_MAP))
			commands.deleteDataMap(datamapURI);
		else if (currentAction.equals(CMD_LOAD_BRAIN_REGIONS))
			commands.loadBrainRegions(file);
		else 
			throw new IllegalArgumentException("Unrecognized command: " + currentAction);
	}

	private boolean parseArguments(String[] args) {
		if (args.length == 0) {
			printerror("Missing arguments");
			return false;
		}
		currentAction = args[0];
		if (currentAction.equals(CMD_LOAD_ATLAS)) {
			if (args.length != 2) {
				printerror("Wrong number of arguments");
				return false;
			}
			File atlasXml = new File(args[1]);
			if (!atlasXml.canRead()) {
				printerror("File " + args[1] + " cannot be red");
				return false;
			}
			if (!atlasXml.isFile()) {
				printerror(args[1] + " is not a file");
				return false;
			}
			this.file = atlasXml;  
		} else if (currentAction.equals(CMD_LIST_ATLASES)) {
			if (args.length > 1)
				this.atlasURI = args[1];
			else
				this.atlasURI = null;
		} else if (currentAction.equals(CMD_DELETE_ATLAS)) {
			if (args.length != 2) {
				printerror("Wrong number of arguments");
				return false;	
			}
				this.atlasURI = args[1];
		} else if (currentAction.equals(CMD_LOAD_DATA_MAP)) {
			if (args.length != 2) {
				printerror("Wrong number of arguments");
				return false;
			}
			File dmXml = new File(args[1]);
			if (!dmXml.canRead()) {
				printerror("File " + args[1] + " cannot be red");
				return false;
			}
			if (!dmXml.isFile()) {
				printerror(args[1] + " is not a file");
				return false;
			}
			this.file = dmXml;  
		} else if (currentAction.equals(CMD_LOAD_DATA_MAPS)) {
			if (args.length != 2) {
				printerror("Wrong number of arguments");
				return false;
			}
			File dir = new File(args[1]);
			if (!dir.canRead()) {
				printerror("File " + args[1] + " cannot be red");
				return false;
			}
			if (!dir.isDirectory()) {
				printerror(args[1] + " is not a directory");
				return false;
			}
			this.file = dir;  
		} else if (currentAction.equals(CMD_LIST_DATA_MAPS)) {
			if (args.length != 2) {
				printerror("Wrong number of arguments");
				return false;
			}
			this.atlasURI = args[1];
		} else if (currentAction.equals(CMD_LIST_DATA_MAP)) {
			if (args.length != 2) {
				printerror("Wrong number of arguments");
				return false;	
			}
				this.datamapURI = args[1];
		} else if (currentAction.equals(CMD_DELETE_DATA_MAP)) {
			if (args.length != 2) {
				printerror("Wrong number of arguments");
				return false;	
			}
				this.datamapURI = args[1];
		} else 		if (currentAction.equals(CMD_LOAD_BRAIN_REGIONS)) {
			if (args.length != 2) {
				printerror("Wrong number of arguments");
				return false;
			}
			File brsXml = new File(args[1]);
			if (!brsXml.canRead()) {
				printerror("File " + args[1] + " cannot be red");
				return false;
			}
			if (!brsXml.isFile()) {
				printerror(args[1] + " is not a file");
				return false;
			}
			this.file = brsXml;  
		} else if (currentAction.equals(CMD_HELP)) {
			// OK. No need to do anything here
		} else {
			printerror("Unrecognized command: " + currentAction);
			return false;
		}
		return true;
	}

	private void printerror(String errorMsg) {
		System.out.println(className + ": " + errorMsg);
	}

}
