import { useEffect, useState } from "react";
import { getExternalWeather } from "../../api/methods";
import Clouds from "../../assets/Clouds.png";
import LogoWhite from "../../assets/PlantOriginal.svg";
import Rain from "../../assets/Rain.png";
import Sun from "../../assets/Sun.png";
import "./header.scss";

const returnCurrentWeatherIcon = (currentWeather) => {
  let icon;
  switch (currentWeather) {
    case "Clouds":
      icon = Clouds;
      break;
    case "Rain":
      icon = Rain;
      break;
    case "Clear":
      icon = Sun;
      break;
    default:
      icon = Sun;
      break;
  }

  return icon;
};

export const Header = () => {
  const [data, setData] = useState();

  useEffect(() => {
    getExternalWeather().then((response) => setData(response));
  }, []);

  return (
    <>
      <div className="header">
        <div className="brand">
          <img src={LogoWhite} className="brand--logo" alt="logo-white" />
          <div className="brand--text">roots</div>
        </div>
        <div className="header--location">
          <div>Cluj-Napoca</div>
          <div className="header--temp">
            <div>{data?.temp}&#8451;</div>
            <img
              src={returnCurrentWeatherIcon(data?.condition)}
              alt="weather"
            />
          </div>
        </div>
      </div>
    </>
  );
};
