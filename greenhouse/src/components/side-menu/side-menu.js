import { useNavigate } from "react-router-dom";
import LogoBlack from "../../assets/PlantBlack.svg";
import { menuItems } from "./menu-items";
import "./side-menu.scss";

const renderMenuItems = (menuItems, handleClick) => {
  const currentLocation = window.location.pathname;

  return menuItems.map((item, i) => {
    return (
      <div
        key={i}
        className={`sidemenu--item ${
          currentLocation === item.path ? "sidemenu--item--active" : ""
        }`}
        onClick={() => handleClick(item.path)}
      >
        {item.name}
      </div>
    );
  });
};

export const SideMenu = () => {
  const navigate = useNavigate();
  const handleClick = (path) => navigate(path);

  return (
    <>
      <div className="sidemenu">
        <div>{renderMenuItems(menuItems, handleClick)}</div>
        <div className="sidemenu--trademark">
          <img
            src={LogoBlack}
            className="sidemenu--trademark--logo"
            alt="logo-black"
          />
          <div style={{ fontWeight: 700 }}>&copy;2024 roots limited</div>
        </div>
      </div>
    </>
  );
};
