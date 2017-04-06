package cn.software.utils;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.UUIDGenerationStrategy;
import org.hibernate.id.uuid.Helper;
import org.hibernate.internal.util.BytesHelper;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @author ：xiaopingzhang8@creditease.cn
 * @Description ：采用hibernate生成Id
 * @ClassName ：UUIDHibernate
 * @date ：2017/4/6 14:29
 */
public class UUIDHibernate implements UUIDGenerationStrategy {
    private final long mostSignificantBits;

    private static UUIDHibernate uuidHibernate = null;

    public static UUIDHibernate getInstance() {
        if (uuidHibernate == null) {
            synchronized (UUIDHibernate.class) {
                if (uuidHibernate == null) {
                    uuidHibernate = new UUIDHibernate();
                }
            }
        }
        return uuidHibernate;
    }

    public int getGeneratedVersion() {
        return 1;
    }

    /**
     * 初始化
     */
    public UUIDHibernate() {
        byte[] hiBits = new byte[8];
        System.arraycopy(Helper.getAddressBytes(), 0, hiBits, 0, 4);
        System.arraycopy(Helper.getJvmIdentifierBytes(), 0, hiBits, 4, 4);
        hiBits[6] &= 0x0f;
        hiBits[6] |= 0x10;
        mostSignificantBits = BytesHelper.asLong(hiBits);
    }

    public UUID generateUUID(SharedSessionContractImplementor sharedSessionContractImplementor) {
        long leastSignificantBits = generateLeastSignificantBits(System.currentTimeMillis());
        return new UUID(mostSignificantBits, leastSignificantBits);
    }

    public long getMostSignificantBits() {
        return mostSignificantBits;
    }

    /**
     * @param seed
     * @return
     */
    public static long generateLeastSignificantBits(long seed) {
        byte[] loBits = new byte[8];
        short hiTime = (short) (seed >>> 32);
        int loTime = (int) seed;
        System.arraycopy(BytesHelper.fromShort(hiTime), 0, loBits, 0, 2);
        System.arraycopy(BytesHelper.fromInt(loTime), 0, loBits, 2, 4);
        System.arraycopy(Helper.getCountBytes(), 0, loBits, 6, 2);
        loBits[0] &= 0x3f;
        loBits[0] |= ((byte) 2 << (byte) 6);
        return BytesHelper.asLong(loBits);
    }

    /**
     * 获取UUID
     *
     * @return
     */
    public static String getUUId() {
        return String.valueOf(getInstance().generateUUID(null));
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool(new ThreadFactory() {
            public Thread newThread(Runnable r) {
                return new Thread(r);
            }
        });
        int i = 0;
        while (i < 10000) {
            executor.execute(new Runnable() {
                public void run() {
                    System.out.println(UUIDHibernate.getUUId());
                }
            });
            i++;
        }

    }

}
