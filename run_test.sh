#!/bin/bash

blockSizes=(8 32 128 512 1024 4096 1048576 4194304)
numJobs=(1 4 8 16 32)
rwmixread=(0.0 0.5 1.0)
#rwmixread=(0.0)

#./bin/ycsb load pmemkv -s -P workloads/workloada -P test.dat

for bs in "${blockSizes[@]}"
do
	#./bin/ycsb load pmemkv -P workloads/workloada -P test.dat -p fieldlength=$bs
	for rwm in "${rwmixread[@]}"
	do
		for t in "${numJobs[@]}"
		do
			wmix=$(echo "1 - $rwm" | bc)
		       	filename="workload_bs_${bs}_t_${t}_rwmix_${rwm}.dat"
		       	#./bin/ycsb run redis -s -P workloads/workloada -P test.dat -p readproportion=$rwm -p updateproportion=$wmix -s -threads $t >> results/redis/$filename
			#./bin/ycsb run memcached -s -P workloads/workloada -P test.dat -p readproportion=$rwm -p updateproportion=$wmix -s -threads $t >> results/memcached/$filename
			./bin/ycsb run pmemkv -s -P workloads/workloada -P test.dat -p readproportion=$rwm -p updateproportion=$wmix -p fieldlength=$bs -s -threads $t >> results/tail_lat_results/$filename
		done
	done
done
