package services.dao;

public class DaoFactory
{
    private static final String DAO_ACCESS = "SqliteFactoryImpl";
    private static DataAccessObject daoFactoryImpl = null;
    private DaoFactory() {}

    public static DataAccessObject getInstance() {
        if (daoFactoryImpl == null)
            try
            {
                daoFactoryImpl = (DataAccessObject) Class.forName("services.dao." + DAO_ACCESS).newInstance();
            }
            catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {}

        return daoFactoryImpl;
    }
}
