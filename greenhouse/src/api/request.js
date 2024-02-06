import axios from "axios";

export const handleSuccessResponse = (response) => {
  console.log({ response });
  const { data, status } = response;
  const { response: nestedResponse } = data;
  if (typeof data !== "string") {
    data.statusCode = status;
  }

  if (nestedResponse) {
    nestedResponse.statusCode = status;
  }

  return nestedResponse || data;
};

export const handleErrorResponse = (error) => {
  const { response } = error;
  const { data, status } = response;

  if (error.code === "ERR_NETWORK") {
    const statusCode = 504;
    return { statusCode, message: error.message };
  }

  if (typeof data !== "string") {
    data.statusCode = status;
  }

  return data;
};

export const request = async (props) => {
  const { method, endpoint, data, params, options = {} } = props;

  const headers = {
    "Content-Type": "application/json",
  };
  const url = `${process.env.REACT_APP_API_URL}${endpoint}`;

  return axios({ url, method, data, headers, params, ...options })
    ?.then(handleSuccessResponse)
    .catch(handleErrorResponse);
};
