package teeth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import generic.util.Generic;

public class Teeth extends Generic<Teeth>{

    int teethiD ;
    // for img manipulation
    int leftlimit ;
    int rightlimit ;
    int toplimit ;
    int bottomlimit ;



        /**
     * Inserts a new Tooth record into the database.
     *
     * @param connection The database connection.
     * @param isTransactional Indicates whether the operation is part of a transaction.
     * @throws SQLException If an SQL error occurs during the database operation.
     */
    public void insert(Connection connection, boolean isTransactional) throws SQLException {
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("insert into teeth values(?,?,?,?,?)");
            preparedStatement.setInt(1, getTeethiD());
            preparedStatement.setInt(2, getLeftlimit());
            preparedStatement.setInt(3, getRightlimit());
            preparedStatement.setInt(4, getToplimit());
            preparedStatement.setInt(5, getBottomlimit());

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            if (!isTransactional) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (!isTransactional) {
                connection.close();
            }
        }
    }

    /**
     * Retrieves an array of Tooth objects from the database.
     *
     * @param connection The database connection.
     * @param isTransactional Indicates whether the operation is part of a transaction.
     * @return An array of Tooth objects.
     * @throws Exception If an error occurs during the database operation.
     */
    public Teeth[] select(Connection connection, boolean isTransactional) throws Exception {
        try {
            ResultSet res = connection.createStatement().executeQuery("select * from Teeth");
            ArrayList<Teeth> teeth = new ArrayList<>();
            while (res.next()) {
                Teeth t = new Teeth();
                t.setTeethiD(res.getInt("Teethid"));
                t.setLeftlimit(res.getInt("leftlimit"));
                t.setRightlimit(res.getInt("rightlimit"));
                t.setToplimit(res.getInt("toplimit"));
                t.setBottomlimit(res.getInt("bottomlimit"));
                teeth.add(t);
            }
            return teeth.toArray(new Teeth[teeth.size()]);
        } catch (Exception e) {
            if (!isTransactional) {
                connection.rollback();
                connection.close();
            }
            throw e;
        }
    }


    public int getTeethiD() {
        return teethiD;
    }
    public void setTeethiD(int teethiD) {
        this.teethiD = teethiD;
    }
    public int getLeftlimit() {
        return leftlimit;
    }
    public void setLeftlimit(int leftlimit) {
        this.leftlimit = leftlimit;
    }
    public int getRightlimit() {
        return rightlimit;
    }
    public void setRightlimit(int rightlimit) {
        this.rightlimit = rightlimit;
    }
    public int getToplimit() {
        return toplimit;
    }
    public void setToplimit(int toplimit) {
        this.toplimit = toplimit;
    }
    public int getBottomlimit() {
        return bottomlimit;
    }
    public void setBottomlimit(int bottomlimit) {
        this.bottomlimit = bottomlimit;
    }

    

}