#!/bin/bash

blockSizes=(1024)
numJobs=(100)
rwmixread=(0.0)
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
			./bin/ycsb run nvmmiddleware -s -P workloads/workloada -P test.dat -p readproportion=$rwm -p updateproportion=$wmix -p fieldlength=$bs -s -threads $t >> results/nvmmiddleware_results/$filename
		done
	done
done
