import { ENDPOINTS } from "./endpoints";
import { request } from "./request";

export const submitDesiredState = (data) =>
  request({ method: "post", endpoint: ENDPOINTS.submitDesiredState, data });

export const getMonitorFromDate = (params) =>
  request({ method: "get", endpoint: ENDPOINTS.getMonitorFromDate, params });

export const getExternalWeather = () =>
  request({ method: "get", endpoint: ENDPOINTS.getExternalWeather });

export const getForecast = () =>
  request({ method: "get", endpoint: ENDPOINTS.getForecast });

export const getCurrentMonitor = () =>
  request({ method: "get", endpoint: ENDPOINTS.getCurrentMonitor });

export const getCommandLogs = () =>
  request({ method: "get", endpoint: ENDPOINTS.getCommandLogs });
