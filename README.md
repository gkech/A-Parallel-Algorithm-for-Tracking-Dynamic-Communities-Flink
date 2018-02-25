# Table of Contents
1. [Parallel Algorithm for Tracking Dynamic Communities](#parallel-algorithm-for-tracking-dynamic-communities)
2. [Preliminaries](#preliminaries)
    1. [Path Settings](#path-settings)
    2. [Start Apache Flink](#start-apache-flink)
    3. [Configure Apache Flink](#configure-apache-flink)
3. [Input Data Format](#input-data-format)
4. [Output Data Format](#output-data-format)
5. [Building](#building)
5. [Authors](#authors)

# Parallel Algorithm for Tracking Dynamic Communities

This algorithm addresses the problem of tracking dynamic communities between consecutive timeframes of a social network, where communities are represented as undirected graphs. It compares the communities based on the widely adopted `Jaccard similarity` measure and is implemented on top of Apache Flink. The implementation is highly modular, as the similarity measure can be easily substituted, and the algorithm can scale to big datasets if sufficient computational resources are available.

It has been tested on Apache Flink 1.3.2 and 1.4 (latest version: 13/2/2018). Nevertheless, it can likely be run and on older versions.

# Preliminaries

## Path Settings

To successfully execute `executeTracking.sh`, first you have to [download](https://flink.apache.org/downloads.html) Apache Flink and add its directory to `$PATH`. For permanent adding, edit `.bashrc` in your home directory. Then, at the end of the document (for convenience) add one of the following options:

```bash
export PATH="/path/to/your/flink/bin:$PATH" # Flink directory is placed first
```

```bash
export PATH="$PATH:/path/to/your/flink/bin" # Flink directory is placed last
```

Usually `.bashrc` is hidden. To unhide it, go to your home directory and press Ctrl-H. Alternatively, select `Show Hidden Files` from the View menu. 

For temporary `$PATH` modification, open a terminal and enter again the following line :

```bash
export PATH="/path/to/your/flink/bin:$PATH" # ordering applies here as well 
```

To check whether `$PATH` has Apache Flink directory, enter the following command:

```bash
echo $PATH
```
If you have successfully added Apache Flink directory in the `$PATH`, the path appeared on terminal should consider it.

## Configure Apache Flink

To configure the parallelism of the tasks, the amount of memory allocated to task managers etc, you have to modify the confucturation of Apache Flink. All configuration is done in conf/flink-conf.yaml, which is expected to be a flat collection of YAML key value pairs with format `key: value`. 

All the available configurations of Apache Flink, are contained [here](https://ci.apache.org/projects/flink/flink-docs-release-1.4/ops/config.html#configuration).

For this project, we suggest the configuration of the following keys value pairs:
1. `taskmanager.heap.mb:`
2. `taskmanager.numberOfTaskSlots:`
3. `parallelism.default:`
4. `akka.framesize:`

## Start Apache Flink

Having finished with `$PATH` settings, the next step is to start a local Flink cluster. To do that, open a terminal and type the following command:

```bash
start-local.sh
```
Starting a local Flink cluster ultimately starts a new JVM. To stop the local Flink Cluster type:

```bash
stop-local.sh
```
You can list the instrumented JVMs on your system using jps tool. Open a terminal and type:

```bash
jps
```

You can check the JobManager’s web frontend at http://localhost:8081 to make sure everything is up and running.

Having the `$PATH` set and Flink cluster configured and started, now you can run the script `executeTracking.sh`

```bash
./executeTracking.sh
```

# Input Data Format

An example of executing the algorithm can be found in `executeTracking.sh`. Downloading this repository and executing this script after "installing" Apache Flink as descibed above, will immediately start the algorithm with default inputs. Nevertheless, for customized execution, modify the inputs of the algorithm, i.e. modify the contents of `executeTracking.sh`. The inputs of the algorithm are summarized on the table below:

|Option| Flag  | isRequired |hasArgument|  Description |
| -------------| ------------- | ------------- | ------------- |------------- |
|Input| -i  | true  | true  |Specify the full path to a dataset.|
|Output| -o  | true  | true  |Specify the path to the output directory.|
|Iterations| -t  | false  | true  |Set the number of times to repeat an experiment, i.e. how many times the algorithm will run. Default value: 1|
|Dataset Name| -d  | false  | true  |Set a prefix to the name of the result files. Default value: "default"|
|Enlarge| -f  | false  | true  |Englarge a 20 timeframes dataset up to 60. This is the highest number of timeframes our method can handle due to Apache Flink limitation. Default value: 1|
|Delete Similarities| -r  | false  | false  |Delete similarities output file to save space.|

Note that the repeats of an experiment (flag `-t`) can be any number bigger than or equal to 1, whereas, the algorithm will throw an `InvalidInputException` if otherwise. Furthermore, the enlargement of a dataset (flag `-f`) considers that the initial dataset contains 20 timeframes. As Apache Flink's Union transformation can handle up to 64 elements, ultimately this means that you can englarge a dataset up to 60 timeframes, that is x3 times. If a dataset contains 10 timeframes, x6 times.
Since we evaluated our method using datasets containing 10 and 20 timeframes, we only allow englargement values [1,3], otherwise a `InvalidInputException` is thrown.

The dataset given as input to the algorithm must adhere to the following JSON template:

```sh
{
    "windows":
     [      //array of windows (i.e. timeframes)
        {
            "communities":
             [     //array of communities in each window
                [     //array of edges in each community
                    [   //array containing two node ids between which an edge exists (this is an edge of the community)
                     ]
                ]
            ]
        }
    ]
}
```
We assume that the dataset has been split into timeframes and that communities have
been discovered in each timeframe. An example of a dataset consisting of two timeframes and their communities is shown below.

```sh
                                             <-------  community ---------->   <-------  community ---------->
timeframe:1 --> {"windows":[{"communities":[ [[id1,id2],[id2,id3],[id3,id1]], [[id4,id5],[id5,id6],[id6,id7]] ]},
timeframe:2 --> {"communities":[ [[id8,id9],[id9,id10],[id10,id1]], [[id11, id12],[id12,id13],[id13,id11]] ] }]}
```

# Output Data Format

The algorith outputs two `.csv` files. One which contains the execution time of each experiment and the average of them, and one which contains the similarities of dynamic communities between consecutive timeframes. An example of the similarities output is shown below:

```bash
[...]
timeframe_18,community_0,timeframe_19,community_2,0.002070393374741201
timeframe_18,community_0,timeframe_19,community_20,0.0
timeframe_18,community_104,timeframe_19,community_84,1.0
timeframe_2,community_32,timeframe_3,community_28,0.7
timeframe_10,community_25,timeframe_11,community_17,1.0
timeframe_8,community_37,timeframe_9,community_29,0.9333333333333333
timeframe_5,community_0,timeframe_6,community_1,0.3042016806722689
[...]
```

# Building

This is a Maven project. For the implementation we used Eclipse Java EE IDE, Version: Neon.3 Release (4.6.3) and Maven plug-in for Eclipse, however, any other IDE compatible with maven (e.g. IntelliJ IDEA) probably will do just as well. To import this project to Eclipse IDE, follow the following simple steps.

1. Open Eclipse
2. Click File > Import
4. Search Maven & Select Existing Maven Projects
5. Click Next
6. Click Browse, find this project's directory, and select the `root_maven` folder which contains the pom.xml file
7. Click Next
8. Click Finish

If the above steps were successful, now you will be have a copy of this project in your IDE, ready to improve or modify it.

# Authors

This project is developed by:

Georgios Kechagias, Technical University of Crete, gkechagias@isc.tuc.gr

Grigorios Tzortzis, NCSR Demokritos, gtzortzi@iit.demokritos.gr	

Dimitrios Vogiatzis, NCSR "Demokritos", dimitrv@iit.demokritos.gr

Georgios Paliouras, NCSR "Demokritos", paliourg@iit.demokritos.gr



