Blaze Spawner Cluster Finder

A lot of people were asking for the source and I've been too lazy to finish and polish this up, so I decided to to release it.

Notes: 

-This implementation is pretty hacky and basically just rips some files directly from decompiled MCP code, so this may be sketchy to put up (not like I'm making money though) but oh well.

-The search is actually pretty optimized: it takes advantage of the fact that nether fortresses in the same world will often have the same "chunk seed" and I keep a cache of structures already generated with this seed. Also I use a kd-tree for a time efficient nearest-neighbor check. Also if I remember correctly some of the LSB of the world seed aren't needed or used in generation.

-I think spawner Y level might be screwed up.

-Use at your own discretion.