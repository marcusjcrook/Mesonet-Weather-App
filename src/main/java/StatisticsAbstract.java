/**
 * Abstract Statistics Class provides abstract methods for getting the minimum
 * and maximum day temp (or srad)
 */
public abstract class StatisticsAbstract
{
    /**
     * abstract method for getting the minimum day (see DayStatistics class)
     * 
     * @param inParamId
     *            the parameter to check
     * @return StatMeasurement object represents the minimum day
     * @throws WrongParameterIdException
     */
    public abstract StatMeasurement getMinimumDay(String inParamId) throws WrongParameterIdException;

    /**
     * abstract method for getting the maximum day (see DayStatistics class)
     * 
     * @param inParamId
     *            the parameter to check
     * @return StatMeasurement object represents the maximum day
     * @throws WrongParameterIdException
     */
    public abstract StatMeasurement getMaximumDay(String inParamId) throws WrongParameterIdException;
}
