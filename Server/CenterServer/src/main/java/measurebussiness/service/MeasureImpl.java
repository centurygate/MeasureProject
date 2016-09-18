package measurebussiness.service;

import measurebussiness.dao.MeasureMapper;
import measurebussiness.model.Measure;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Created by free on 2016/9/6.
 */
public class MeasureImpl implements IMeasureService
{
	@Autowired
	private MeasureMapper measureDao = null;
	
	public int addMeasureRecord(Measure measure)
	{
		return this.measureDao.insert(measure);
	}
	
	public List<Measure> queryMeasureByAddrTime(String devAddr, Date startTime, Date endTime)
	{
		return this.measureDao.queryMeasureByAddrTime(devAddr,startTime,endTime);
	}
}
