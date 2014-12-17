package cn.itcast.jbpm4console.domain;

import java.util.Date;

import org.jbpm.api.history.HistoryTask;
import org.jbpm.pvm.internal.history.model.HistoryTaskInstanceImpl;

public class HistoryTaskView {
	private HistoryTask historyTask;
	private HistoryTaskInstanceImpl historyTaskInstanceImpl;

	public HistoryTaskView(HistoryTask historyTask, HistoryTaskInstanceImpl historyTaskInstanceImpl) {
		this.historyTask = historyTask;
		this.historyTaskInstanceImpl = historyTaskInstanceImpl;
	}

	/**
	 * 任务名称
	 * 
	 * @return
	 */
	public String getName() {
		return historyTaskInstanceImpl.getActivityName();
	}

	public String getId() {
		return historyTask.getId();
	}

	public String getAssignee() {
		return historyTask.getAssignee();
	}

	public Date getCreateTime() {
		return historyTask.getCreateTime();
	}

	public long getDuration() {
		return historyTask.getDuration();
	}

	public Date getEndTime() {
		return historyTask.getEndTime();
	}

	public String getExecutionId() {
		return historyTask.getExecutionId();
	}

	public String getOutcome() {
		return historyTask.getOutcome();
	}

	public String getState() {
		return historyTask.getState();
	}
}
