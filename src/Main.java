import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import net.sf.javaml.core.kdtree.KDTree;

import java.util.List;
import java.util.Map;

public class Main
{
	public static Random baserand = new Random();
	public static List<Vec3i> spawners = new LinkedList<>();
	
	public static int currentXChunkPos, currentZChunkPos, currentXOR;
	public static Map<Integer, List<Vec3i>> cachedSpawnersForXOR = new HashMap<>();
	
	public static void getSpawnersInChunk(long seed, int x, int z)
	{
		/*baserand.setSeed(seed);
        long j = baserand.nextLong();
        long k = baserand.nextLong();
        
        long j1 = (long)x * j;
        long k1 = (long)z * k;
        baserand.setSeed(j1 ^ k1 ^ seed);
        
        baserand.nextInt();*/
        
		
		
        int i2 = x >> 4;
        int j2 = z >> 4;
        
        
        
        baserand.setSeed((long)(i2 ^ j2 << 4) ^ seed);
        baserand.nextInt();
        
        
        if (baserand.nextInt(3) != 0)
        {
        }
        else if (x != (i2 << 4) + 4 + baserand.nextInt(8))
        {
        }
        else
        {
            
        	if(z == (j2 << 4) + 4 + baserand.nextInt(8))
            {
        		//System.out.println("X: " + x + " Z: " + z);
        		
        		currentXChunkPos = x*16;
        		currentZChunkPos = z*16;
        		
        		currentXOR = i2^j2<<4;
                
                if(cachedSpawnersForXOR.containsKey(new Integer(currentXOR)))
                {
                	for(Vec3i v : cachedSpawnersForXOR.get(currentXOR))
                	{
                		spawners.add(new Vec3i(currentXChunkPos + v.getX(), v.getY(), currentZChunkPos + v.getZ()));
                	}
                	return;
                }
                cachedSpawnersForXOR.put(new Integer(currentXOR), new LinkedList<>());
        		
        		StructureNetherBridgePieces.startBuild(baserand, x, z);
            }
        }
	}
	
	public static void main(String [] args) throws IOException
	{
		/*while(true)
		{
		baserand.setSeed(System.currentTimeMillis());
		long seed = baserand.nextLong();
		
		for(int i = -20; i < 20; i++)
		{
			for(int j = -20; j < 20; j++)
			{
				getSpawnerList(i*50 - 33, i*50 + 33, j*50 - 33, j*50 + 33, seed, baserand);
				
				for(Vec3i v : spawners)
				{
					int count = 0;
					for(Vec3i b : spawners)
					{
						if((v.getX()-b.getX())*(v.getX()-b.getX())+(v.getZ()-b.getZ())*(v.getZ()-b.getZ())<677 && v != b)
						{
							count++;
						}
					}
					
					if(count >= 3)
					{
						System.out.println("X: " + v.getX()  + " Z: " + v.getZ() + " Number: " + count + " Seed: " + seed);
					}
				}
				
				spawners.clear();
			}
		}
		}*/
		
		//long time1 = System.currentTimeMillis();
		
		for(long seedind = 251000; seedind < Integer.MAX_VALUE; seedind++)
		{
			if(seedind%1000==0)System.out.println(seedind);
		getSpawnerList(-1000, 1000, -1000, 1000, seedind<<16, baserand);
		
		KDTree tree = new KDTree(3);
		for(Vec3i v : spawners)
		{
			tree.insert(new double[]{(double)v.getX(),(double)v.getY(), (double)v.getZ()}, new double[]{(double)v.getX(),(double)v.getY(), (double)v.getZ()});
		}
		/*Vec3i n = spawners.get(10);
		System.out.println(n.getX() + " " + n.getY() + " " + n.getZ());
		Object[] nearest = tree.nearest(new double[] {n.getX(), n.getY(), n.getZ()}, 2);
		
		System.out.println(Arrays.toString((double[])nearest[0]) + " " + Arrays.toString((double[])nearest[1]));*/
		
		int spawnernum = 3;
		int dist = 31*31;
		//System.out.println(spawners.size());
		
		for(Vec3i v : spawners)
		{
			double[] curvec = new double[] {(double)v.getX(), (double)v.getY(), (double)v.getZ()};
			Object[] nearest = tree.nearest(curvec, spawnernum + 1);
			
			/*for(Object o : nearest)
			{
				System.out.print(Arrays.toString((double[])o));
			}*/
			
			int found=1;
			for(int i = 1; i < spawnernum+1; i++)
			{
				if(!(squareDist(curvec, (double[])nearest[i]) < dist))
				{
					found=0;
					break;
				}
			}
			if(found==1)
			{
				for(double q = curvec[0]-16; q <= curvec[0] + 16; q+= .5)
				{
					for(double w = curvec[1]-16; w <= curvec[1] + 16; w+= .5)
					{
						for(double e = curvec[2]-16; e <= curvec[2] + 16; e+= .5)
						{
							boolean allLessThan16 = true;
							for(int r = 0; r < spawnernum+1; r++)
							{
								if(!(squareDist(new double[] {q, w, e}, (double[])nearest[r]) < 256))
								{
									allLessThan16=false;
									break;
								}
							}
							if(allLessThan16)
							{
								found=2;
								break;
							}
						}
						if(found==2)break;
					}
					if(found==2)break;
				}
				
				
				
				if(found==2)
				{
					System.out.println(Arrays.toString(curvec) + " seed: " + (seedind<<16));
					String fileContent = Arrays.toString(curvec) + " seed: " + (seedind<<16);
				     
				    BufferedWriter writer = new BufferedWriter(new FileWriter("blaze", true));
				    writer.write(fileContent);
				    writer.newLine();
				    //System.out.println("1");
				    //writer.append(fileContent);
				    writer.close();
				}
				
				
				
				
			}
		}
		
		//System.out.println(System.currentTimeMillis()-time1);
		spawners.clear();
		cachedSpawnersForXOR.clear();
		}
	}
	
	public static void getSpawnerList(int xchunkmin, int xchunkmax, int zchunkmin, int zchunkmax, long seed, Random rand)
	{
		for(int x = xchunkmin; x <= xchunkmax; x++)
		{
			for(int z = zchunkmin; z <= zchunkmax; z++)
			{
				getSpawnersInChunk(seed, x, z);
			}
		}
	}
	
	static double squareDist(double[] n1, double []n2)
	{
		double dist =0;
		for(int i = 0; i < n1.length; i++)
		{
			dist+=(n1[i]-n2[i])*(n1[i]-n2[i]);
		}
		
		//System.out.println(dist);
		return dist;
	}
}
