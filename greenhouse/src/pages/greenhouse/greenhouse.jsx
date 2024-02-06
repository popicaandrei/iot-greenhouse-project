import dayjs from "dayjs";
import { useEffect, useRef, useState } from "react";
import {
  VictoryAxis,
  VictoryChart,
  VictoryLine,
  VictoryTooltip,
  VictoryVoronoiContainer,
} from "victory";
import { getCurrentMonitor, getMonitorFromDate } from "../../api/methods";
import { custom } from "../weather-forecast/chart-theme";
import "./greenhouse.scss";

export const Greenhouse = () => {
  const timerIdRef = useRef(null);
  const [data, setData] = useState([]);

  const [currentTemp, setCurrentTemp] = useState(21);
  const [currentHum, setCurrentHum] = useState(40);
  const yesterday = dayjs()
    .subtract(1, "day")
    .startOf("day")
    .format("YYYY-MM-DD");

  useEffect(() => {
    getMonitorFromDate({ fromDate: yesterday }).then((response) => {
      console.log({ response });
      setData(response);
    });
  }, []);

  useEffect(() => {
    const pollingCallback = () => {
      // Your polling logic here
      console.log("Polling...");

      getCurrentMonitor().then((response) => {
        setCurrentTemp(response?.temperature);
        setCurrentHum(response?.humidity);
      });
    };

    const startPolling = () => {
      timerIdRef.current = setInterval(pollingCallback, 3000);
    };

    const stopPolling = () => {
      console.log("stopped polling");
      clearInterval(timerIdRef.current);
    };

    if (window.location.pathname === "/greenhouse") {
      console.log("startedPolling");
      startPolling();
    } else {
      stopPolling();
    }

    return () => {
      stopPolling();
    };
  }, []);

  return (
    <div className="greenhouse">
      <div className="double-chart">
        <div className="double-chart--container">
          <div style={{ color: "white", fontSize: "28px", fontWeight: "700" }}>
            Live temp: {currentTemp}&#x2103;
          </div>
          <div className="double-chart--container--text">Temperature</div>
          <VictoryChart
            padding={{ top: 50, bottom: 50, right: 0, left: 50 }}
            domainPadding={40}
            theme={custom}
            containerComponent={<VictoryVoronoiContainer />}
          >
            <VictoryAxis
              style={{
                grid: { stroke: "#F4F5F7", strokeWidth: 0.2 },
              }}
              tickCount={5}
            />
            <VictoryAxis
              style={{
                grid: { stroke: "#F4F5F7", strokeWidth: 0.2 },
              }}
              dependentAxis
              tickFormat={(x) => `${x}\u{00B0}`}
              tickCount={7}
            />
            <VictoryLine
              data={data?.map((x) => {
                return {
                  ...x,
                  label: `${x.temperature}\u{00B0}, ${dayjs(x.timestamp).format(
                    "HH:mm:ss"
                  )}`,
                  timestamp: dayjs(x.timestamp).format("HH:mm"),
                };
              })}
              x="timestamp"
              y="temperature"
              labelComponent={
                <VictoryTooltip
                  flyoutStyle={{
                    stroke: "none",
                    fill: "invisible",
                  }}
                />
              }
            />
          </VictoryChart>
        </div>

        <div className="double-chart--container">
          <div style={{ color: "white", fontSize: "28px", fontWeight: "700" }}>
            Live humidity: {currentHum}%
          </div>

          <div className="double-chart--container--text">Humidity</div>
          <VictoryChart
            padding={{ top: 50, bottom: 50, right: 0, left: 50 }}
            domainPadding={40}
            theme={custom}
            containerComponent={<VictoryVoronoiContainer />}
          >
            <VictoryAxis
              style={{
                grid: { stroke: "#F4F5F7", strokeWidth: 0.2 },
              }}
              tickCount={7}
            />
            <VictoryAxis
              style={{
                grid: { stroke: "#F4F5F7", strokeWidth: 0.2 },
              }}
              dependentAxis
              tickFormat={(x) => `${x}%`}
            />
            <VictoryLine
              data={data?.map((x) => {
                return {
                  ...x,
                  label: `${x.humidity}%, ${dayjs(x.timestamp).format(
                    "HH:mm:ss"
                  )}`,
                  timestamp: dayjs(x.timestamp).format("HH:mm"),
                };
              })}
              x="timestamp"
              y="humidity"
              labelComponent={
                <VictoryTooltip
                  flyoutStyle={{
                    stroke: "none",
                    fill: "invisible",
                  }}
                />
              }
            />
          </VictoryChart>
        </div>
      </div>
    </div>
  );
};
