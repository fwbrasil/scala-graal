package bench;

import java.util.Random;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public class HashSwitchBench {

  int[]  values = { 10, 20, 30, 40, 50, 60, 70, 80, 90, 100 };
  Random r      = new Random(1);

  // @CompilerControl(CompilerControl.Mode.DONT_INLINE)
  // private int sparse(int i) {
  // int v = -1;
  // switch (i) {
  // case 10:
  // v = 10;
  // break;
  // case 20:
  // v = 20;
  // break;
  // case 30:
  // v = 30;
  // break;
  // case 40:
  // v = 40;
  // break;
  // case 50:
  // v = 50;
  // break;
  // case 60:
  // v = 60;
  // break;
  // case 70:
  // v = 70;
  // break;
  // case 80:
  // v = 80;
  // break;
  // case 90:
  // v = 90;
  // break;
  // case 100:
  // v = 100;
  // break;
  // default:
  // v = 110;
  // }
  // return v;
  // }
  //
  // @Benchmark
  // public int bench() {
  // int i = values[r.nextInt(values.length)];
  // int v = sparse(i);
  // if (v != i)
  // throw new RuntimeException("" + v + " " + i);
  // return v;
  // }

  static int[] httpCodes = { 100, 101, 102, 103, 200, 201, 202, 203, 204, 205,
      206,
      207,
      208,
      226,
      300,
      301,
      302,
      303,
      304,
      305,
      306,
      307,
      308,
      400,
      401,
      402,
      403,
      404,
      405,
      406,
      407,
      408,
      409,
      410,
      411,
      412,
      413,
      414,
      415,
      416,
      417,
      421,
      422,
      423,
      424,
      425,
      426,
      427,
      428,
      429,
      430,
      431,
      451,
      500,
      501,
      502,
      503,
      504,
      505,
      506,
      507,
      508,
      509,
      510,
      511
  };

  @Benchmark
  public String httpStatus() {
    int i = httpCodes[r.nextInt(httpCodes.length)];
    switch (i) {
      case 100:
        return "Continue";
      case 101:
        return "Switching Protocols";
      case 102:
        return "Processing";
      case 103:
        return "Early Hints";
      case 200:
        return "OK";
      case 201:
        return "Created";
      case 202:
        return "Accepted";
      case 203:
        return "Non-Authoritative Information";
      case 204:
        return "No Content";
      case 205:
        return "Reset Content";
      case 206:
        return "Partial Content";
      case 207:
        return "Multi-Status";
      case 208:
        return "Already Reported";
      case 226:
        return "IM Used";
      case 300:
        return "Multiple Choices";
      case 301:
        return "Moved Permanently";
      case 302:
        return "Found";
      case 303:
        return "See Other";
      case 304:
        return "Not Modified";
      case 305:
        return "Use Proxy";
      case 306:
        return "(Unused)";
      case 307:
        return "Temporary Redirect";
      case 308:
        return "Permanent Redirect";
      case 400:
        return "Bad Request";
      case 401:
        return "Unauthorized";
      case 402:
        return "Payment Required";
      case 403:
        return "Forbidden";
      case 404:
        return "Not Found";
      case 405:
        return "Method Not Allowed";
      case 406:
        return "Not Acceptable";
      case 407:
        return "Proxy Authentication Required";
      case 408:
        return "Request Timeout";
      case 409:
        return "Conflict";
      case 410:
        return "Gone";
      case 411:
        return "Length Required";
      case 412:
        return "Precondition Failed";
      case 413:
        return "Payload Too Large";
      case 414:
        return "URI Too Long";
      case 415:
        return "Unsupported Media Type";
      case 416:
        return "Range Not Satisfiable";
      case 417:
        return "Expectation Failed";
      case 421:
        return "Misdirected Request";
      case 422:
        return "Unprocessable Entity";
      case 423:
        return "Locked";
      case 424:
        return "Failed Dependency";
      case 425:
        return "Too Early";
      case 426:
        return "Upgrade Required";
      case 428:
        return "Precondition Required";
      case 429:
        return "Too Many Requests";
      case 431:
        return "Request Header Fields Too Large";
      case 451:
        return "Unavailable For Legal Reasons";
      case 500:
        return "Internal Server Error";
      case 501:
        return "Not Implemented";
      case 502:
        return "Bad Gateway";
      case 503:
        return "Service Unavailable";
      case 504:
        return "Gateway Timeout";
      case 505:
        return "HTTP Version Not Supported";
      case 506:
        return "Variant Also Negotiates";
      case 507:
        return "Insufficient Storage";
      case 508:
        return "Loop Detected";
      case 510:
        return "Not Extended";
      case 511:
        return "Network Authentication Required";
      default:
        return "Unassigned";
    }

  }
}
