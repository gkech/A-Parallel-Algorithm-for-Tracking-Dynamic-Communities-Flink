#!/bin/bash

DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

cd $DIR

#auto create an output directory
if [ ! -d output/ ]; then
  mkdir -p output/;
  echo "output foloder created"
fi

#mandatory
datasetLocation=dataset_example/community_edges.json
#mandatory, flink needs full path to sink directory
outputLocation=$DIR/output/


#optional, default value: 1
repeats=1
#optional, default value: "default"
datasetName=crimea
#optional, default value: 1
enlargeDataset=1


# an example of how to execute the algorithm is shown below, with inputs only the dataset and the output directory
flink run -c iit.flink.ct.root.CommunityTrackingIterative flink.ct-1.0.jar -i=$datasetLocation -o=$outputLocation #-t=$repeats -d=$datasetName -f=$enlargeDataset -r																																																																						

																																							
