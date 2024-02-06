import "./App.scss";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { Header, SideMenu } from "./components";
import { ControlPanel, Greenhouse, WeatherForecast } from "./pages";

function App() {
  return (
    <BrowserRouter>
      <div className="app">
        <Header />
        <div className="app--content">
          <SideMenu />
          <div className="app--content--page">
            <Routes>
              <Route path="/weather-forecast" element={<WeatherForecast />} />
              <Route path="/greenhouse" element={<Greenhouse />} />
              <Route path="/control-panel" element={<ControlPanel />} />
            </Routes>
          </div>
        </div>
      </div>
    </BrowserRouter>
  );
}

export default App;
