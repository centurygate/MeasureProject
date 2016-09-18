package measurebussiness.service;

import measurebussiness.model.Measure;

import java.util.List;

/**
 * Created by free on 2016/9/6.
 */
public interface IMeasureService
{
	int addMeasureRecord(Measure measure);
	List<Measure> queryMeasureByAddrTime(String devAddr, Long startTime,Long endTime);
}
