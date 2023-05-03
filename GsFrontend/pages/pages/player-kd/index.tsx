import { Column } from 'primereact/column';
import TopService from '../../../demo/service/TopService';
import TopResultTable from '../../../demo/components/TopResultTable';
import { PlayerKdBucket } from '../../../demo/service/TopService';

const WinRatioPage = () => {

    const playerKd = (column: PlayerKdBucket) => {
        if(column?.kd) {
            return column?.kd.toFixed(3);
        }
        return null;
    }

    return (
        <TopResultTable
            title="Top Player K/D"
            getGlobalRanking={(tags) => TopService.getTopPlayerKd(tags)}
            columns={
                [<Column header="Player Name" field="playerName" />,
                <Column header="K/D" body={playerKd} />,
                <Column header="Kills"  field="PGS_PILOT_KILLS" />,
                <Column header="Deaths" field="PGS_DEATHS" />]}/>
    );
};

export default WinRatioPage;
