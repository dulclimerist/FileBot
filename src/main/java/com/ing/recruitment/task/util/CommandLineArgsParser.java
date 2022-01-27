package com.ing.recruitment.task.util;

import org.apache.commons.cli.*;

public class CommandLineArgsParser {
    private String workingDirectory;
    private String scriptsFile;
    private Options options;

    public CommandLineArgsParser() {
        prepareOptions();
    }

    private void prepareOptions() {
        options = new Options();

        Option directory = Option.builder(null)
                .longOpt("dir")
                .hasArg()
                .desc("Destination directory")
                .build();

        Option scripts = Option.builder(null)
                .longOpt("scripts")
                .hasArg()
                .desc("Scripts file name")
                .build();

        Option help = Option.builder(null)
                .longOpt("help")
                .desc("Show help message")
                .build();

        options.addOption(directory);
        options.addOption(scripts);
        options.addOption(help);
    }

    public boolean parse(String[] args) {
        CommandLineParser parser = new DefaultParser();
        boolean result = true;
        try {
            CommandLine cmd = parser.parse(options, args);

            if(cmd.hasOption("help")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("FileBot", options);
                return false;
            }

            if(!cmd.hasOption("dir")) {
                System.out.println("Please provide working directory location");
                result = false;
            } else {
                workingDirectory = cmd.getOptionValue("dir");
            }

            if(!cmd.hasOption("scripts")) {
                System.out.println("Please provide scripts file");
                result = false;
            } else {
                scriptsFile = cmd.getOptionValue("scripts");
            }
        } catch (ParseException e) {
            System.out.println("Incorrect command line arguments: " + e.getMessage());
            System.out.println("Please use --help to show usage manual");
            result = false;
        }
        return result;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public String getScriptsFile() {
        return scriptsFile;
    }
}
