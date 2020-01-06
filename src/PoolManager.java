import java.util.HashSet;
import java.util.Queue;
import java.util.LinkedList;

public class PoolManager
{

    protected PoolResource[] res;

    Queue<Integer> idleResources;
    HashSet<Integer> busyResources;


    public PoolManager(final int resCount,final PoolResource generator)throws Exception
    {

        System.out.println("@PoolManager.PoolManager");

        idleResources = new LinkedList<Integer>();
        busyResources = new HashSet<Integer>();

        res = new PoolResource[resCount];

        for(int i=0;i<resCount;i++)
        {

            res[i] = generator.getObject(this,i);

            idleResources.add(i);

        }

        System.out.println("resources allocated for pool manager");

    }

    public void free(int index)
    {

        System.out.println("@PoolManager.free");

        if(busyResources.remove(index))
        {

            idleResources.add(index);

        }
    }

    public boolean isResourceAvailable()
    {

        System.out.println("@PoolManager.isREsourceAvailable");

        return (idleResources.size() > 0);

    }

    public PoolResource getPoolResource() throws Exception
    {

        System.out.println("@PoolManager.getPoolResources");

        if(idleResources.size() == 0)throw new Exception("no resource availble");

        int resIndex = idleResources.iterator().next();

        idleResources.remove(resIndex);
        busyResources.add(resIndex);

        return res[resIndex];

    }

}