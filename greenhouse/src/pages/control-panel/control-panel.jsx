import { Button } from "../../components";
import "./control-panel.scss";
import ArrowUp from "../../assets/chevron-top-normal.svg";
import ArrowDown from "../../assets/chevron-bottom-normal.svg";
import { useEffect, useRef, useState } from "react";
import { getCommandLogs, submitDesiredState } from "../../api/methods";
import dayjs from "dayjs";

const renderCommandRows = (commands) => {
  return commands.map((command, index) => {
    return (
      <div
        key={index}
        className={`row row--text ${index % 2 === 0 ? "row--shade" : ""}`}
      >
        <div className="row--field">{command.id}</div>
        <div className="row--field">{command.heaterSwitch ? "ON" : "OFF"}</div>
        <div className="row--field">{command.fanSwitch ? "ON" : "OFF"}</div>
        <div className="row--field">
          {dayjs(command.timestamp).format("YYYY-MM-DD HH:mm")}
        </div>
      </div>
    );
  });
};

export const ControlPanel = () => {
  const timerPanelIdRef = useRef(null);

  const [temp, setTemp] = useState(25);
  const [hum, setHum] = useState(50);

  const [commands, setCommands] = useState([]);

  useEffect(() => {
    getCommandLogs().then((response) => {
      setCommands(response?.slice(-5).reverse());
    });
  }, []);

  useEffect(() => {
    const pollingCallback = () => {
      // Your polling logic here
      console.log("Polling...");

      getCommandLogs().then((response) => {
        setCommands(response?.slice(-5).reverse());
      });
    };

    const startPolling = () => {
      timerPanelIdRef.current = setInterval(pollingCallback, 5000);
    };

    const stopPolling = () => {
      console.log("stopped polling");
      clearInterval(timerPanelIdRef.current);
    };

    if (window.location.pathname === "/control-panel") {
      console.log("startedPolling");
      startPolling();
    } else {
      stopPolling();
    }

    return () => {
      stopPolling();
    };
  }, []);

  const handleClick = () => {
    submitDesiredState({ temperature: temp, humidity: hum });
  };

  return (
    <div className="control-page">
      <div className="control-text">Control the parameters manually</div>

      <div className="control-page--block">
        <div>
          <div className="control-page--group">
            <div className="control-page--group--input">
              <div className="control-parameter">{temp}</div>
              <div className="control-page--group--arrows">
                <img
                  src={ArrowUp}
                  alt="arrowUp"
                  className="control-page--group--arrows--size"
                  onClick={() => setTemp(temp + 1)}
                />
                <img
                  src={ArrowDown}
                  alt="arrowDown"
                  className="control-page--group--arrows--size"
                  onClick={() => setTemp(temp - 1)}
                />
              </div>
            </div>
            <div className="label-text">Temperature </div>
          </div>
        </div>
        <Button
          color="pink"
          text="Send command"
          minWidth={250}
          onClick={handleClick}
        />
      </div>

      <div className="command-block">
        <div>
          <div className="command-block--text">Command history</div>
        </div>
        <div className="command-block--container">
          <div className="command-block--container--header">
            <div className="command-block--container--header-item">Id</div>{" "}
            <div className="command-block--container--header-item">
              Heater Switch
            </div>
            <div className="command-block--container--header-item">
              Fan Switch
            </div>
            <div className="command-block--container--header-item">
              {" "}
              Timestamp
            </div>
          </div>
          {renderCommandRows(commands)}
        </div>
      </div>
    </div>
  );
};
