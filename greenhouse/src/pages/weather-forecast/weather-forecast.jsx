import dayjs from "dayjs";
import { useEffect, useState } from "react";
import {
  VictoryAxis,
  VictoryChart,
  VictoryLine,
  VictoryTooltip,
  VictoryVoronoiContainer,
} from "victory";
import { getForecast } from "../../api/methods";
import { Button } from "../../components";
import { custom } from "./chart-theme";
import "./weather-forecast.scss";

export const WeatherForecast = () => {
  const today = dayjs().endOf("day");
  const tomorrow = dayjs().add(1, "days").endOf("day");
  const dayAfterTomorrow = dayjs().add(2, "days").endOf("day");

  const [data, setData] = useState();
  const [period, setPeriod] = useState();
  const [graphData, setGraphData] = useState(data);

  useEffect(() => {
    getForecast()
      .then((response) => {
        const aux = response?.map((x) => {
          return { ...x, timestamp: dayjs(x.timestamp) };
        });
        setData(aux);
      })
      .then(() => {
        setPeriod(today);
      });
    // eslint-disable-next-line
  }, []);

  useEffect(() => {
    const timedData = data
      ?.filter((x) => x.timestamp.day() === period.day())
      ?.map((x) => {
        return { ...x, timestamp: x.timestamp.format("HH:mm") };
      });
    setGraphData(timedData);
    // eslint-disable-next-line
  }, [period]);

  return (
    <div className="weather-forecast">
      <div className="weather-forecast--actions">
        <div className="weather-forecast--actions--message">
          Select the day:
        </div>
        <div className="weather-forecast--actions--buttons">
          <Button
            color="green"
            text="Today"
            onClick={() => setPeriod(today)}
            minWidth={250}
          />
          <Button
            color="green"
            text="Tomorrow"
            onClick={() => setPeriod(tomorrow)}
            minWidth={250}
          />
          <Button
            color="green"
            text="Day after tomorrow"
            onClick={() => setPeriod(dayAfterTomorrow)}
            minWidth={250}
          />
        </div>
      </div>
      <div className="chart">
        <div className="chart--container">
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
            />
            <VictoryAxis
              style={{
                grid: { stroke: "#F4F5F7", strokeWidth: 0.2 },
              }}
              dependentAxis
              tickFormat={(x) => `${x}\u{00B0}`}
            />
            <VictoryLine
              data={graphData?.map((x) => {
                return { ...x, label: x.temp };
              })}
              x="timestamp"
              y="temp"
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
